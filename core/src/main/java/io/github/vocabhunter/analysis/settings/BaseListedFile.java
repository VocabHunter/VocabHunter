/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.vocabhunter.analysis.settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.nio.file.Path;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SessionListedFile.class, name = "SESSION"),
    @JsonSubTypes.Type(value = DocumentListedFile.class, name = "DOCUMENT"),
    @JsonSubTypes.Type(value = ExcelListedFile.class, name = "EXCEL")})
public class BaseListedFile {
    private final Path file;

    @JsonCreator
    protected BaseListedFile(
        @JsonProperty("file")
        final Path file) {
        this.file = file;
    }

    public Path getFile() {
        return file;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BaseListedFile that = (BaseListedFile) o;

        return new EqualsBuilder()
            .append(file, that.file)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(file)
            .toHashCode();
    }
}
