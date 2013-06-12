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
 * on 2013-06-11 at 21:59:45 UTC 
 * Modify at your own risk.
 */

package com.frca.gamingscheduler.messageEndpoint;

/**
 * MessageEndpoint request initializer for setting properties like key and userIp.
 * <p/>
 * <p>
 * The simplest usage is to use it to set the key parameter:
 * </p>
 * <p/>
 * <pre>
 * public static final GoogleClientRequestInitializer KEY_INITIALIZER =
 * new MessageEndpointRequestInitializer(KEY);
 * </pre>
 * <p/>
 * <p>
 * There is also a constructor to set both the key and userIp parameters:
 * </p>
 * <p/>
 * <pre>
 * public static final GoogleClientRequestInitializer INITIALIZER =
 * new MessageEndpointRequestInitializer(KEY, USER_IP);
 * </pre>
 * <p/>
 * <p>
 * If you want to implement custom logic, extend it like this:
 * </p>
 * <p/>
 * <pre>
 * public static class MyRequestInitializer extends MessageEndpointRequestInitializer {
 *
 * {@literal @}Override
 * public void initializeMessageEndpointRequest(MessageEndpointRequest{@literal <}?{@literal >} request)
 * throws IOException {
 * // custom logic
 * }
 * }
 * </pre>
 * <p/>
 * <p>
 * Finally, to set the key and userIp parameters and insert custom logic, extend it like this:
 * </p>
 * <p/>
 * <pre>
 * public static class MyRequestInitializer2 extends MessageEndpointRequestInitializer {
 *
 * public MyKeyRequestInitializer() {
 * super(KEY, USER_IP);
 * }
 *
 * {@literal @}Override
 * public void initializeMessageEndpointRequest(MessageEndpointRequest{@literal <}?{@literal >} request)
 * throws IOException {
 * // custom logic
 * }
 * }
 * </pre>
 * <p/>
 * <p>
 * Subclasses should be thread-safe.
 * </p>
 *
 * @since 1.12
 */
public class MessageEndpointRequestInitializer extends com.google.api.client.googleapis.services.json.CommonGoogleJsonClientRequestInitializer {

    public MessageEndpointRequestInitializer() {
        super();
    }

    /**
     * @param key API key or {@code null} to leave it unchanged
     */
    public MessageEndpointRequestInitializer(String key) {
        super(key);
    }

    /**
     * @param key    API key or {@code null} to leave it unchanged
     * @param userIp user IP or {@code null} to leave it unchanged
     */
    public MessageEndpointRequestInitializer(String key, String userIp) {
        super(key, userIp);
    }

    @Override
    public final void initializeJsonRequest(com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest<?> request) throws java.io.IOException {
        super.initializeJsonRequest(request);
        initializeMessageEndpointRequest((MessageEndpointRequest<?>) request);
    }

    /**
     * Initializes MessageEndpoint request.
     * <p/>
     * <p>
     * Default implementation does nothing. Called from
     * {@link #initializeJsonRequest(com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest)}.
     * </p>
     *
     * @throws java.io.IOException I/O exception
     */
    protected void initializeMessageEndpointRequest(MessageEndpointRequest<?> request) throws java.io.IOException {
    }
}
