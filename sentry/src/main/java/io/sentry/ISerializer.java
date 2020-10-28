package io.sentry;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import io.sentry.protocol.User;

public interface ISerializer {
  SentryEvent deserializeEvent(Reader reader);

  UserFeedback deserializeUserFeedback(Reader reader);

  Session deserializeSession(Reader reader);

  SentryEnvelope deserializeEnvelope(InputStream inputStream);

  void serialize(SentryEvent event, Writer writer) throws IOException;

  void serialize(Session session, Writer writer) throws IOException;

  void serialize(UserFeedback userFeedback, Writer writer) throws IOException;

  void serialize(SentryEnvelope envelope, Writer writer) throws Exception;

  String serialize(Map<String, Object> data) throws Exception;
}
