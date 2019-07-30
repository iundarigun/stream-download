package br.com.devcave.streamdownload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RestController
public class DownloadController {

    @GetMapping(value ="/download")
    public ResponseEntity<StreamingResponseBody> download(final HttpServletResponse response){
        response.setContentType("application/zip");
        response.setHeader(
                "Content-Disposition",
                "attachment;filename=sample.zip");
        StreamingResponseBody stream = out -> {

            final File directory = new File("/home/ocanalias/workspace/99taxi/documents/testes/B2B2-151");
            final ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());

            if(directory.exists() && directory.isDirectory()) {
                try {
                    for (final File file : directory.listFiles()) {
                        final InputStream inputStream=new FileInputStream(file);
                        final ZipEntry zipEntry=new ZipEntry(file.getName());
                        zipOut.putNextEntry(zipEntry);
                        byte[] bytes=new byte[1024];
                        int length;
                        while ((length=inputStream.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                        inputStream.close();
                    }
                    zipOut.close();
                } catch (final IOException e) {
                    log.error("Exception while reading and streaming data {} ", e);
                }
            }
        };
        log.info("steaming response {} ", stream);
        return new ResponseEntity(stream, HttpStatus.OK);
    }
}
