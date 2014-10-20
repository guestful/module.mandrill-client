/**
 * Copyright (C) 2013 Guestful (info@guestful.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.guestful.client.mandrill;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MandrillClient {

    private static final Logger LOGGER = Logger.getLogger(MandrillClient.class.getName());

    private final Client client;
    private final WebTarget target;
    private final MandrillConfig config;
    private boolean enabled = true;
    private final MandrillTemplate emptyTemplate;

    public MandrillClient(Client restClient) {
        this(restClient, new MandrillConfig());
    }

    public MandrillClient(MandrillConfig config) {
        this(ClientBuilder.newClient(), config);
    }

    public MandrillClient(Client restClient, MandrillConfig config) {
        this.client = restClient;
        this.target = buildWebTarget();
        this.config = config;
        this.emptyTemplate = new MandrillTemplate(this, null);
    }

    public Client getClient() {
        return client;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public MandrillTemplate getTemplate(String slugName) {
        return new MandrillTemplate(this, slugName);
    }

    public MandrillMessage createMandrillMessage() {
        return new MandrillMessage(this);
    }

    public MandrillConfig getConfig() {
        return config;
    }

    protected WebTarget buildWebTarget() {
        return getClient().target("https://mandrillapp.com/api/1.0");
    }

    Response request(String method, String path, JsonObject message) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            if (message.containsKey("message")) {
                JsonObject msg = message.getJsonObject("message");
                if (msg.containsKey("html") || msg.containsKey("text")) {
                    String str = message.toString();
                    if (str.length() > 1000) {
                        LOGGER.finest(method + " " + path + " : " + str.substring(0, 1000));
                    } else {
                        LOGGER.finest(method + " " + path + " : " + str);
                    }
                } else {
                    LOGGER.finest(method + " " + path + " : " + message);
                }
            } else {
                LOGGER.finest(method + " " + path + " : " + message);
            }
        }
        return isEnabled() ?
            target
                .path(path)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .method(method, Entity.json(message)) :
            Response.ok().build();
    }

}
