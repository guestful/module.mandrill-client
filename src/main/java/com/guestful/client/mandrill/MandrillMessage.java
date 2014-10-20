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

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MandrillMessage {

    private final MandrillClient client;

    private MandrillConfig config;
    private MandrillAddress from;
    private String subject;
    private String html;
    private String text;
    private final Collection<MandrillAddress> tos = new ArrayList<>();
    private final MandrillVars globalMergeVars = new MandrillVars();
    private final Map<String, MandrillVars> mergeVars = new TreeMap<>();
    private String slug;

    MandrillMessage(MandrillClient client) {
        this.client = client;
        this.config = client.getConfig().copy();
    }

    public String getSlug() {
        return slug;
    }

    public MandrillClient getClient() {
        return client;
    }

    public MandrillMessage preserveRecipients(boolean preserveRecipients) {
        config.setPreserveRecipients(preserveRecipients);
        return this;
    }

    public MandrillMessage bcc(String bccAddress) {
        config.setBccAddress(bccAddress);
        return this;
    }

    public MandrillMessage trackingDomain(String trackingDomain) {
        config.setTrackingDomain(trackingDomain);
        return this;
    }

    public MandrillMessage signingDomain(String signingDomain) {
        config.setSigningDomain(signingDomain);
        return this;
    }

    public MandrillMessage trackOpens(boolean trackOpens) {
        config.setTrackOpens(trackOpens);
        return this;
    }

    public MandrillMessage trackClicks(boolean trackClicks) {
        config.setTrackClicks(trackClicks);
        return this;
    }

    public MandrillMessage tag(String tag) {
        config.addTags(tag);
        return this;
    }

    public MandrillMessage tags(String... tags) {
        return tags(Arrays.asList(tags));
    }

    public MandrillMessage tags(Iterable<String> tags) {
        config.addTags(tags);
        return this;
    }

    public MandrillMessage from(String name, String email) {
        if (email != null) {
            from = new MandrillAddress(name, email);
        }
        return this;
    }

    public MandrillMessage from(MandrillAddress from) {
        this.from = from;
        return this;
    }

    public MandrillMessage from(String email) {
        this.from = new MandrillAddress(email);
        return this;
    }

    public MandrillMessage subject(String subject) {
        this.subject = subject;
        return this;
    }

    public MandrillMessage slug(String slugName) {
        this.slug = slugName;
        return this;
    }

    public MandrillMessage html(String html) {
        this.html = html;
        return this;
    }

    public MandrillMessage text(String text) {
        this.text = text;
        return this;
    }

    public MandrillMessage tos(Collection<MandrillAddress> addresses) {
        tos.addAll(addresses);
        return this;
    }

    public MandrillMessage to(MandrillAddress addr) {
        tos.add(addr);
        return this;
    }

    public MandrillMessage to(String email) {
        tos.add(new MandrillAddress(email));
        return this;
    }

    public MandrillMessage to(String name, String email) {
        tos.add(new MandrillAddress(name, email));
        return this;
    }

    public MandrillMessage tos(Map<String, String> emailNames) {
        for (Map.Entry<String, String> entry : emailNames.entrySet()) {
            to(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public MandrillVars getGlobalMergeVars() {
        return globalMergeVars;
    }

    public MandrillMessage set(String name, boolean value) {
        globalMergeVars.set(name, value);
        return this;
    }

    public MandrillMessage set(String name, BigInteger value) {
        globalMergeVars.set(name, value);
        return this;
    }

    public MandrillMessage set(String name, long value) {
        globalMergeVars.set(name, value);
        return this;
    }

    public MandrillMessage set(String name, int value) {
        globalMergeVars.set(name, value);
        return this;
    }

    public MandrillMessage set(String name, BigDecimal value) {
        globalMergeVars.set(name, value);
        return this;
    }

    public MandrillMessage set(String name, double value) {
        globalMergeVars.set(name, value);
        return this;
    }

    public MandrillMessage set(String name, String value) {
        globalMergeVars.set(name, value);
        return this;
    }

    public MandrillVars getMergeVars(String email) {
        MandrillVars vars = mergeVars.get(email);
        if (vars == null) {
            vars = new MandrillVars();
            mergeVars.put(email, vars);
        }
        return vars;
    }

    public JsonObject toJson() {
        JsonArrayBuilder _tags = Json.createArrayBuilder();
        config.getTags().forEach(_tags::add);

        JsonArrayBuilder _tos = Json.createArrayBuilder();
        this.tos.forEach(to -> _tos.add(to.toJson()));

        JsonArrayBuilder _merge_vars = Json.createArrayBuilder();
        this.mergeVars.forEach((k, v) ->
            _merge_vars.add(Json.createObjectBuilder()
                .add("rcpt", k)
                .add("vars", v.toJson())));

        JsonObjectBuilder message = Json.createObjectBuilder()
            .add("to", _tos)
            .add("tags", _tags)
            .add("preserve_recipients", config.isPreserveRecipients())
            .add("track_opens", config.isTrackOpens())
            .add("track_clicks", config.isTrackClicks())
            .add("global_merge_vars", getGlobalMergeVars().toJson())
            .add("merge_vars", _merge_vars);
        if (config.getBccAddress() != null) message.add("bcc_address", config.getBccAddress());
        if (config.getSigningDomain() != null) message.add("signing_domain", config.getSigningDomain());
        if (from != null && from.getName() != null) message.add("from_name", from.getName());
        if (from != null && from.getEmail() != null) message.add("from_email", from.getEmail());
        if (subject != null) message.add("subject", subject);
        if (text != null || html != null) {
            message.add("merge", false);
        }
        if (text != null) message.add("text", text);
        if (html != null) message.add("html", html);

        return message.build();
    }

    public void send() throws MandrillException {
        send(toJson());
    }

    public void send(JsonObject message) throws MandrillException {
        JsonObjectBuilder bodyBuilder = Json.createObjectBuilder()
            .add("key", config.getApiKey())
            .add("async", config.isAsync())
            .add("message", message);
        if (slug != null) {
            bodyBuilder.add("template_name", slug);
            bodyBuilder.add("template_content", Json.createArrayBuilder());
        }
        JsonObject body = bodyBuilder.build();
        String uri = body.containsKey("template_name") ? "messages/send-template.json" : "messages/send.json";
        Response response = getClient().request(HttpMethod.POST, uri, body);
        try {
            if (response.getStatus() != 200) {
                throw new MandrillException(response, body);
            }
        } finally {
            response.close();
        }
    }

    public boolean hasForm() {
        return from != null;
    }

}
