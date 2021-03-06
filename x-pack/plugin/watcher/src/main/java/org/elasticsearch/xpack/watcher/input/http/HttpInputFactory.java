/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.watcher.input.http;

import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.xpack.watcher.common.http.HttpClient;
import org.elasticsearch.xpack.watcher.common.http.HttpRequestTemplate;
import org.elasticsearch.xpack.watcher.common.text.TextTemplateEngine;
import org.elasticsearch.xpack.watcher.input.InputFactory;

import java.io.IOException;

public final class HttpInputFactory extends InputFactory<HttpInput, HttpInput.Result, ExecutableHttpInput> {

    private final HttpClient httpClient;
    private final TextTemplateEngine templateEngine;
    private final HttpRequestTemplate.Parser requestTemplateParser;

    public HttpInputFactory(Settings settings, HttpClient httpClient, TextTemplateEngine templateEngine,
                            HttpRequestTemplate.Parser requestTemplateParser) {
        super(Loggers.getLogger(ExecutableHttpInput.class, settings));
        this.templateEngine = templateEngine;
        this.httpClient = httpClient;
        this.requestTemplateParser = requestTemplateParser;
    }

    @Override
    public String type() {
        return HttpInput.TYPE;
    }

    @Override
    public HttpInput parseInput(String watchId, XContentParser parser) throws IOException {
        return HttpInput.parse(watchId, parser, requestTemplateParser);
    }

    @Override
    public ExecutableHttpInput createExecutable(HttpInput input) {
        return new ExecutableHttpInput(input, inputLogger, httpClient, templateEngine);
    }
}
