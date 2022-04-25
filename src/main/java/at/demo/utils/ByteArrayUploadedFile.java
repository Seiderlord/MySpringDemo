package at.demo.utils;

import org.primefaces.model.file.UploadedFile;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ByteArrayUploadedFile implements UploadedFile {
    private final byte [] data;

    private final String filename;

    private final String contentType;

    public ByteArrayUploadedFile(byte [] data, String filename, String contentType) {
        this.data = data;
        this.filename = filename;
        this.contentType = contentType;
    }

    @Override
    public String getFileName() {
        return filename;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }

    @Override
    public long getSize() {
        return data.length;
    }

    @Override
    public byte[] getContent() {
        return data;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void write(String filePath) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
        }
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException();
    }
}
