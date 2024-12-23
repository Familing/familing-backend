package com.pinu.familing.global.log.service;

import com.pinu.familing.global.log.dto.LogResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
@Service
@Slf4j
@RequiredArgsConstructor
public class LogService {
    private volatile long lastReadPosition = 0;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static final int BUFFER_SIZE = 8192;
    private static final Pattern SQL_PATTERN = Pattern.compile("\\b(SELECT|INSERT|UPDATE|DELETE)\\b");
    private static final Pattern ERROR_PATTERN = Pattern.compile("\\b(ERROR|Exception|Failed|Failure)\\b");
    private static final Pattern INFO_PATTERN = Pattern.compile("INFO");

    @Value("${log.file.path}")
    private String logFilePath;

    public LogResponse getLogsByPage(int page, int size, boolean includeErrors, boolean includeSQL, boolean includeInfo) {
        try (RandomAccessFile file = new RandomAccessFile(logFilePath, "r")) {
            int totalLines = countTotalLines(file);
            long startPosition = calculateStartPosition(file, page, size, totalLines);
            List<String> logs = readLogsFromPosition(file, startPosition, size)
                    .stream()
                    .filter(log -> filterLog(log, includeErrors, includeSQL, includeInfo))
                    .collect(Collectors.toList());

            return new LogResponse(logs, totalLines, page, size);
        } catch (IOException e) {
            log.error("로그 파일 읽기 실패: {}", e.getMessage());
            throw new RuntimeException("로그 파일을 읽을 수 없습니다.", e);
        }
    }

    private int countTotalLines(RandomAccessFile file) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        long fileLength = file.length();

        return StreamSupport.stream(
                        new FileBufferSpliterator(file, buffer, 0, fileLength), true)
                .mapToInt(this::countNewlines)
                .sum() + 1;
    }

    private int countNewlines(byte[] buffer) {
        return (int) IntStream.range(0, buffer.length)
                .filter(i -> buffer[i] == '\n')
                .count();
    }

    private long calculateStartPosition(RandomAccessFile file, int page, int size, int totalLines) throws IOException {
        int linesToSkip = totalLines - ((page + 1) * size);
        if (linesToSkip <= 0) return 0;

        long avgBytesPerLine = file.length() / totalLines;
        long estimatedPosition = Math.max(0, avgBytesPerLine * linesToSkip);

        file.seek(estimatedPosition);
        file.readLine();
        return file.getFilePointer();
    }

    private List<String> readLogsFromPosition(RandomAccessFile file, long position, int size) throws IOException {
        file.seek(position);
        List<String> logs = new ArrayList<>();
        StringBuilder currentLog = new StringBuilder();
        String line;

        while ((line = file.readLine()) != null) {
            if (line.startsWith("Hibernate:")) {
                // 이전 로그 저장
                if (currentLog.length() > 0) {
                    logs.add(currentLog.toString());
                    currentLog.setLength(0); // StringBuilder 초기화
                }
                currentLog.append(line); // 새로운 Hibernate: 로그 시작
            } else if (line.startsWith("    ")) {
                // Hibernate:로 시작하지 않지만 들여쓰기가 있다면 SQL의 연속
                currentLog.append("\n").append(line.trim());
            } else {
                // 새로운 로그가 시작되었다면 현재 로그를 저장
                if (currentLog.length() > 0) {
                    logs.add(currentLog.toString());
                    currentLog.setLength(0);
                }
                logs.add(line); // 새로운 로그 추가
            }

            // 로그 수가 size에 도달하면 중지
            if (logs.size() >= size) {
                break;
            }
        }

        // 남아있는 로그 추가
        if (currentLog.length() > 0) {
            logs.add(currentLog.toString());
        }

        return logs;
    }

    private boolean filterLog(String log, boolean includeErrors, boolean includeSQL, boolean includeInfo) {
        boolean isError = ERROR_PATTERN.matcher(log).find();
        boolean isSQL = log.startsWith("Hibernate:") || SQL_PATTERN.matcher(log).find();
        boolean isInfo = log.contains("INFO");

        return (includeErrors && isError) || (includeSQL && isSQL) || (includeInfo && isInfo);
    }

    private List<String> removeDuplicates(List<String> logs) {
        return new ArrayList<>(new LinkedHashSet<>(logs));
    }

    public List<String> getNewLogs(boolean includeErrors, boolean includeSQL, boolean includeInfo) {
        rwLock.readLock().lock();
        try (RandomAccessFile file = new RandomAccessFile(logFilePath, "r")) {
            List<String> newLogs = processNewLogs(file);
            return removeDuplicates(
                    newLogs.stream()
                            .filter(log -> filterLog(log, includeErrors, includeSQL, includeInfo))
                            .collect(Collectors.toList())
            );
        } catch (IOException e) {
            log.error("새로운 로그 읽기 실패: {}", e.getMessage());
            return new ArrayList<>();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    private List<String> processNewLogs(RandomAccessFile file) throws IOException {
        long fileLength = file.length();
        if (fileLength < lastReadPosition) {
            updateLastReadPosition(0);
        }

        if (fileLength > lastReadPosition) {
            List<String> newLogs = readNewLogs(file);
            updateLastReadPosition(file.getFilePointer());
            return newLogs;
        }

        return new ArrayList<>();
    }

    private List<String> readNewLogs(RandomAccessFile file) throws IOException {
        file.seek(lastReadPosition);
        List<String> newLogs = new ArrayList<>();
        String line;
        long currentPosition = lastReadPosition;

        while ((line = file.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            newLogs.add(line);
            currentPosition = file.getFilePointer();
        }

        lastReadPosition = currentPosition;
        return newLogs;
    }

    private void updateLastReadPosition(long position) {
        rwLock.readLock().unlock();
        rwLock.writeLock().lock();
        try {
            lastReadPosition = position;
        } finally {
            rwLock.writeLock().unlock();
            rwLock.readLock().lock();
        }
    }

    private static class FileBufferSpliterator extends Spliterators.AbstractSpliterator<byte[]> {
        private final RandomAccessFile file;
        private final byte[] buffer;
        private long position;
        private final long end;

        FileBufferSpliterator(RandomAccessFile file, byte[] buffer, long position, long end) {
            super(Long.MAX_VALUE, Spliterator.ORDERED);
            this.file = file;
            this.buffer = buffer;
            this.position = position;
            this.end = end;
        }

        @Override
        public boolean tryAdvance(Consumer<? super byte[]> action) {
            try {
                if (position >= end) return false;
                file.seek(position);
                int bytesRead = file.read(buffer, 0, (int) Math.min(buffer.length, end - position));
                if (bytesRead < 0) return false;
                action.accept(Arrays.copyOf(buffer, bytesRead));
                position += bytesRead;
                return true;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
