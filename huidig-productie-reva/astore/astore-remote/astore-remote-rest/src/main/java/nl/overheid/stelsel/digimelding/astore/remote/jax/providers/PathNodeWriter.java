/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package nl.overheid.stelsel.digimelding.astore.remote.jax.providers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.clerezza.web.fileserver.util.MediaTypeGuesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wymiwyg.commons.util.dirbrowser.PathNode;

/**
 * 
 */
@Provider
public class PathNodeWriter implements MessageBodyWriter<PathNode> {

  private final Logger logger = LoggerFactory.getLogger(PathNodeWriter.class);
  private String cacheControlHeaderValue = "max-age=600";

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType) {
    return PathNode.class.isAssignableFrom(type);
  }

  @Override
  public long getSize(PathNode t, Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType) {
    return t.getLength();
  }

  @Override
  public void writeTo(PathNode t, Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
      throws IOException, WebApplicationException {
    logger.debug("Invoked with media type: {}", mediaType);
    logger.debug("Invoked with pathnode: {}", t);
    if (mediaType.equals(MediaType.APPLICATION_OCTET_STREAM_TYPE)) {
      MediaType guessedMediaType = MediaTypeGuesser.getInstance().guessTypeForName(t.getPath());
      if (guessedMediaType != null) {
        httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, guessedMediaType);
        logger.debug("Set media-type to: {}", guessedMediaType);
      }
    }
    if (!httpHeaders.containsKey(HttpHeaders.CACHE_CONTROL)) {
      httpHeaders.putSingle(HttpHeaders.CACHE_CONTROL, cacheControlHeaderValue);
    } else {
      logger.debug("httpHeaders already contain CACHE_CONTROL");
    }
    InputStream in = t.getInputStream();
    for (int ch = in.read(); ch != -1; ch = in.read()) {
      entityStream.write(ch);
    }
  }
}
