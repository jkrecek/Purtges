/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2013-06-05 16:09:48 UTC)
 * on 2013-06-13 at 09:49:13 UTC
 * Modify at your own risk.
 */

package com.frca.gamingscheduler.userdeviceendpoint;

/**
 * Service definition for Userdeviceendpoint (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link UserdeviceendpointRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Userdeviceendpoint extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.15.0-rc of the  library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://gamingscheduler.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "userdeviceendpoint/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Userdeviceendpoint(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  Userdeviceendpoint(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "getUserDevice".
   *
   * This request holds the parameters needed by the the userdeviceendpoint server.  After setting any
   * optional parameters, call the {@link GetUserDevice#execute()} method to invoke the remote
   * operation.
   *
   * @param id
   * @return the request
   */
  public GetUserDevice getUserDevice(java.lang.String id) throws java.io.IOException {
    GetUserDevice result = new GetUserDevice(id);
    initialize(result);
    return result;
  }

  public class GetUserDevice extends UserdeviceendpointRequest<com.frca.gamingscheduler.userdeviceendpoint.model.UserDevice> {

    private static final String REST_PATH = "userdevice/{id}";

    /**
     * Create a request for the method "getUserDevice".
     *
     * This request holds the parameters needed by the the userdeviceendpoint server.  After setting
     * any optional parameters, call the {@link GetUserDevice#execute()} method to invoke the remote
     * operation. <p> {@link GetUserDevice#initialize(com.google.api.client.googleapis.services.Abstra
     * ctGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected GetUserDevice(java.lang.String id) {
      super(Userdeviceendpoint.this, "GET", REST_PATH, null, com.frca.gamingscheduler.userdeviceendpoint.model.UserDevice.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetUserDevice setAlt(java.lang.String alt) {
      return (GetUserDevice) super.setAlt(alt);
    }

    @Override
    public GetUserDevice setFields(java.lang.String fields) {
      return (GetUserDevice) super.setFields(fields);
    }

    @Override
    public GetUserDevice setKey(java.lang.String key) {
      return (GetUserDevice) super.setKey(key);
    }

    @Override
    public GetUserDevice setOauthToken(java.lang.String oauthToken) {
      return (GetUserDevice) super.setOauthToken(oauthToken);
    }

    @Override
    public GetUserDevice setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetUserDevice) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetUserDevice setQuotaUser(java.lang.String quotaUser) {
      return (GetUserDevice) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetUserDevice setUserIp(java.lang.String userIp) {
      return (GetUserDevice) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String id;

    /**

     */
    public java.lang.String getId() {
      return id;
    }

    public GetUserDevice setId(java.lang.String id) {
      this.id = id;
      return this;
    }

    @Override
    public GetUserDevice set(String parameterName, Object value) {
      return (GetUserDevice) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "insertUserDevice".
   *
   * This request holds the parameters needed by the the userdeviceendpoint server.  After setting any
   * optional parameters, call the {@link InsertUserDevice#execute()} method to invoke the remote
   * operation.
   *
   * @param registrationId
   * @return the request
   */
  public InsertUserDevice insertUserDevice(java.lang.String registrationId) throws java.io.IOException {
    InsertUserDevice result = new InsertUserDevice(registrationId);
    initialize(result);
    return result;
  }

  public class InsertUserDevice extends UserdeviceendpointRequest<com.frca.gamingscheduler.userdeviceendpoint.model.UserDevice> {

    private static final String REST_PATH = "userdevice/{registrationId}";

    /**
     * Create a request for the method "insertUserDevice".
     *
     * This request holds the parameters needed by the the userdeviceendpoint server.  After setting
     * any optional parameters, call the {@link InsertUserDevice#execute()} method to invoke the
     * remote operation. <p> {@link InsertUserDevice#initialize(com.google.api.client.googleapis.servi
     * ces.AbstractGoogleClientRequest)} must be called to initialize this instance immediately after
     * invoking the constructor. </p>
     *
     * @param registrationId
     * @since 1.13
     */
    protected InsertUserDevice(java.lang.String registrationId) {
      super(Userdeviceendpoint.this, "POST", REST_PATH, null, com.frca.gamingscheduler.userdeviceendpoint.model.UserDevice.class);
      this.registrationId = com.google.api.client.util.Preconditions.checkNotNull(registrationId, "Required parameter registrationId must be specified.");
    }

    @Override
    public InsertUserDevice setAlt(java.lang.String alt) {
      return (InsertUserDevice) super.setAlt(alt);
    }

    @Override
    public InsertUserDevice setFields(java.lang.String fields) {
      return (InsertUserDevice) super.setFields(fields);
    }

    @Override
    public InsertUserDevice setKey(java.lang.String key) {
      return (InsertUserDevice) super.setKey(key);
    }

    @Override
    public InsertUserDevice setOauthToken(java.lang.String oauthToken) {
      return (InsertUserDevice) super.setOauthToken(oauthToken);
    }

    @Override
    public InsertUserDevice setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (InsertUserDevice) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public InsertUserDevice setQuotaUser(java.lang.String quotaUser) {
      return (InsertUserDevice) super.setQuotaUser(quotaUser);
    }

    @Override
    public InsertUserDevice setUserIp(java.lang.String userIp) {
      return (InsertUserDevice) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String registrationId;

    /**

     */
    public java.lang.String getRegistrationId() {
      return registrationId;
    }

    public InsertUserDevice setRegistrationId(java.lang.String registrationId) {
      this.registrationId = registrationId;
      return this;
    }

    @Override
    public InsertUserDevice set(String parameterName, Object value) {
      return (InsertUserDevice) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "listUserDevice".
   *
   * This request holds the parameters needed by the the userdeviceendpoint server.  After setting any
   * optional parameters, call the {@link ListUserDevice#execute()} method to invoke the remote
   * operation.
   *
   * @return the request
   */
  public ListUserDevice listUserDevice() throws java.io.IOException {
    ListUserDevice result = new ListUserDevice();
    initialize(result);
    return result;
  }

  public class ListUserDevice extends UserdeviceendpointRequest<com.frca.gamingscheduler.userdeviceendpoint.model.CollectionResponseUserDevice> {

    private static final String REST_PATH = "userdevice";

    /**
     * Create a request for the method "listUserDevice".
     *
     * This request holds the parameters needed by the the userdeviceendpoint server.  After setting
     * any optional parameters, call the {@link ListUserDevice#execute()} method to invoke the remote
     * operation. <p> {@link ListUserDevice#initialize(com.google.api.client.googleapis.services.Abstr
     * actGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @since 1.13
     */
    protected ListUserDevice() {
      super(Userdeviceendpoint.this, "GET", REST_PATH, null, com.frca.gamingscheduler.userdeviceendpoint.model.CollectionResponseUserDevice.class);
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public ListUserDevice setAlt(java.lang.String alt) {
      return (ListUserDevice) super.setAlt(alt);
    }

    @Override
    public ListUserDevice setFields(java.lang.String fields) {
      return (ListUserDevice) super.setFields(fields);
    }

    @Override
    public ListUserDevice setKey(java.lang.String key) {
      return (ListUserDevice) super.setKey(key);
    }

    @Override
    public ListUserDevice setOauthToken(java.lang.String oauthToken) {
      return (ListUserDevice) super.setOauthToken(oauthToken);
    }

    @Override
    public ListUserDevice setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (ListUserDevice) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public ListUserDevice setQuotaUser(java.lang.String quotaUser) {
      return (ListUserDevice) super.setQuotaUser(quotaUser);
    }

    @Override
    public ListUserDevice setUserIp(java.lang.String userIp) {
      return (ListUserDevice) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String cursor;

    /**

     */
    public java.lang.String getCursor() {
      return cursor;
    }

    public ListUserDevice setCursor(java.lang.String cursor) {
      this.cursor = cursor;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Integer limit;

    /**

     */
    public java.lang.Integer getLimit() {
      return limit;
    }

    public ListUserDevice setLimit(java.lang.Integer limit) {
      this.limit = limit;
      return this;
    }

    @Override
    public ListUserDevice set(String parameterName, Object value) {
      return (ListUserDevice) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "removeUserDevice".
   *
   * This request holds the parameters needed by the the userdeviceendpoint server.  After setting any
   * optional parameters, call the {@link RemoveUserDevice#execute()} method to invoke the remote
   * operation.
   *
   * @param id
   * @return the request
   */
  public RemoveUserDevice removeUserDevice(java.lang.String id) throws java.io.IOException {
    RemoveUserDevice result = new RemoveUserDevice(id);
    initialize(result);
    return result;
  }

  public class RemoveUserDevice extends UserdeviceendpointRequest<com.frca.gamingscheduler.userdeviceendpoint.model.UserDevice> {

    private static final String REST_PATH = "userdevice/{id}";

    /**
     * Create a request for the method "removeUserDevice".
     *
     * This request holds the parameters needed by the the userdeviceendpoint server.  After setting
     * any optional parameters, call the {@link RemoveUserDevice#execute()} method to invoke the
     * remote operation. <p> {@link RemoveUserDevice#initialize(com.google.api.client.googleapis.servi
     * ces.AbstractGoogleClientRequest)} must be called to initialize this instance immediately after
     * invoking the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected RemoveUserDevice(java.lang.String id) {
      super(Userdeviceendpoint.this, "DELETE", REST_PATH, null, com.frca.gamingscheduler.userdeviceendpoint.model.UserDevice.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public RemoveUserDevice setAlt(java.lang.String alt) {
      return (RemoveUserDevice) super.setAlt(alt);
    }

    @Override
    public RemoveUserDevice setFields(java.lang.String fields) {
      return (RemoveUserDevice) super.setFields(fields);
    }

    @Override
    public RemoveUserDevice setKey(java.lang.String key) {
      return (RemoveUserDevice) super.setKey(key);
    }

    @Override
    public RemoveUserDevice setOauthToken(java.lang.String oauthToken) {
      return (RemoveUserDevice) super.setOauthToken(oauthToken);
    }

    @Override
    public RemoveUserDevice setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (RemoveUserDevice) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public RemoveUserDevice setQuotaUser(java.lang.String quotaUser) {
      return (RemoveUserDevice) super.setQuotaUser(quotaUser);
    }

    @Override
    public RemoveUserDevice setUserIp(java.lang.String userIp) {
      return (RemoveUserDevice) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String id;

    /**

     */
    public java.lang.String getId() {
      return id;
    }

    public RemoveUserDevice setId(java.lang.String id) {
      this.id = id;
      return this;
    }

    @Override
    public RemoveUserDevice set(String parameterName, Object value) {
      return (RemoveUserDevice) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "updateUserDevice".
   *
   * This request holds the parameters needed by the the userdeviceendpoint server.  After setting any
   * optional parameters, call the {@link UpdateUserDevice#execute()} method to invoke the remote
   * operation.
   *
   * @param content the {@link com.frca.gamingscheduler.userdeviceendpoint.model.UserDevice}
   * @return the request
   */
  public UpdateUserDevice updateUserDevice(com.frca.gamingscheduler.userdeviceendpoint.model.UserDevice content) throws java.io.IOException {
    UpdateUserDevice result = new UpdateUserDevice(content);
    initialize(result);
    return result;
  }

  public class UpdateUserDevice extends UserdeviceendpointRequest<com.frca.gamingscheduler.userdeviceendpoint.model.UserDevice> {

    private static final String REST_PATH = "userdevice";

    /**
     * Create a request for the method "updateUserDevice".
     *
     * This request holds the parameters needed by the the userdeviceendpoint server.  After setting
     * any optional parameters, call the {@link UpdateUserDevice#execute()} method to invoke the
     * remote operation. <p> {@link UpdateUserDevice#initialize(com.google.api.client.googleapis.servi
     * ces.AbstractGoogleClientRequest)} must be called to initialize this instance immediately after
     * invoking the constructor. </p>
     *
     * @param content the {@link com.frca.gamingscheduler.userdeviceendpoint.model.UserDevice}
     * @since 1.13
     */
    protected UpdateUserDevice(com.frca.gamingscheduler.userdeviceendpoint.model.UserDevice content) {
      super(Userdeviceendpoint.this, "PUT", REST_PATH, content, com.frca.gamingscheduler.userdeviceendpoint.model.UserDevice.class);
    }

    @Override
    public UpdateUserDevice setAlt(java.lang.String alt) {
      return (UpdateUserDevice) super.setAlt(alt);
    }

    @Override
    public UpdateUserDevice setFields(java.lang.String fields) {
      return (UpdateUserDevice) super.setFields(fields);
    }

    @Override
    public UpdateUserDevice setKey(java.lang.String key) {
      return (UpdateUserDevice) super.setKey(key);
    }

    @Override
    public UpdateUserDevice setOauthToken(java.lang.String oauthToken) {
      return (UpdateUserDevice) super.setOauthToken(oauthToken);
    }

    @Override
    public UpdateUserDevice setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (UpdateUserDevice) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public UpdateUserDevice setQuotaUser(java.lang.String quotaUser) {
      return (UpdateUserDevice) super.setQuotaUser(quotaUser);
    }

    @Override
    public UpdateUserDevice setUserIp(java.lang.String userIp) {
      return (UpdateUserDevice) super.setUserIp(userIp);
    }

    @Override
    public UpdateUserDevice set(String parameterName, Object value) {
      return (UpdateUserDevice) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link Userdeviceendpoint}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          Userdeviceendpoint.DEFAULT_ROOT_URL,
          Userdeviceendpoint.DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link Userdeviceendpoint}. */
    @Override
    public Userdeviceendpoint build() {
      return new Userdeviceendpoint(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link UserdeviceendpointRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setUserdeviceendpointRequestInitializer(
        UserdeviceendpointRequestInitializer userdeviceendpointRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(userdeviceendpointRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}