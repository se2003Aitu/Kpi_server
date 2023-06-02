package com.program.model.submission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data@NoArgsConstructor
public class Submission {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String submissionId;

    private String fileName;
    private String fileType;
    private long size;

    @Lob
    private byte[] data;


    public Submission(String fileName, String fileType, Long size, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
        this.data = data;
    }

    @JsonProperty("submission_id")
    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    @JsonProperty("file_name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonProperty("file_type")
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @JsonProperty("size")
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @JsonIgnore
    public byte[] getData() {
        return data;
    }


    public void setData(byte[] data) {
        this.data = data;
    }
}
