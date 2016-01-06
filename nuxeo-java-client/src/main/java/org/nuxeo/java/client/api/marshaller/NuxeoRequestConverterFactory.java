/*
 * (C) Copyright 2016 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *         Vladimir Pasquier <vpasquier@nuxeo.com>
 */
package org.nuxeo.java.client.api.marshaller;

import java.io.IOException;

import org.nuxeo.java.client.api.ConstantsV1;

import retrofit2.Converter;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import okhttp3.RequestBody;

/**
 * @since 1.0
 */
public final class NuxeoRequestConverterFactory<T> implements Converter<T, RequestBody> {

    protected ObjectWriter adapter;

    protected ObjectMapper objectMapper;

    protected NuxeoMarshaller<T> nuxeoMarshaller;

    NuxeoRequestConverterFactory(ObjectWriter adapter) {
        this.adapter = adapter;
    }

    NuxeoRequestConverterFactory(NuxeoMarshaller<T> nuxeoMarshaller, ObjectMapper objectMapper) {
        this.nuxeoMarshaller = nuxeoMarshaller;
        this.objectMapper = objectMapper;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        ByteArrayBuilder bb = new ByteArrayBuilder();
        byte[] bytes;
        if (nuxeoMarshaller != null) {
            JsonGenerator jg = objectMapper.getFactory().createGenerator(bb, JsonEncoding.UTF8);
            nuxeoMarshaller.write(jg, value);
            bytes = bb.toByteArray();
        } else {
            bytes = adapter.writeValueAsBytes(value);
        }
        return RequestBody.create(ConstantsV1.APPLICATION_JSON_CHARSET_UTF_8, bytes);
    }
}
