package jp.co.benesse.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

/**
 * <pre>
 * ファイルアクセスに関するユーティリティクラス.
 *
 * 作成日：2024/06/16
 * 更新日：2024/06/16
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
public class FileAccessUtil {

    /** キャッシュ */
    private static final Map<String, String> cache = new ConcurrentHashMap<>();

    /** インスタンス化防止用のプライベートコンストラクタ */
    private FileAccessUtil() {
    }

    /**
     * 指定されたファイルを読み込んで文字列で返す
     * 
     * @param file 読み込みたいファイル名
     * @throws UncheckedIOException
     * @return ファイルの中身の文字列
     */
    public static String load(String file) {
        return cache.computeIfAbsent(file, f -> {
            try (InputStream stream = new ClassPathResource(file).getInputStream()) {
                return StreamUtils.copyToString(stream, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

}
