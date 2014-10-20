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

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MandrillConfig {

    private String apiKey;
    private String signingDomain;
    private boolean preserveRecipients = false;
    private boolean async = true;
    private String bccAddress;
    private String trackingDomain;
    private boolean trackOpens = true;
    private boolean trackClicks = true;
    private final Collection<String> tags = new TreeSet<>();

    public MandrillConfig copy() {
        return new MandrillConfig()
            .setApiKey(apiKey)
            .setSigningDomain(signingDomain)
            .setPreserveRecipients(preserveRecipients)
            .setAsync(async)
            .setBccAddress(bccAddress)
            .setTrackingDomain(trackingDomain)
            .setTrackOpens(trackOpens)
            .setTrackClicks(trackClicks)
            .addTags(tags);
    }

    public boolean isAsync() {
        return async;
    }

    public MandrillConfig setAsync(boolean async) {
        this.async = async;
        return this;
    }

    public String getApiKey() {
        return apiKey;
    }

    public MandrillConfig setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public String getSigningDomain() {
        return signingDomain;
    }

    public MandrillConfig setSigningDomain(String signingDomain) {
        this.signingDomain = signingDomain;
        return this;
    }

    public boolean isPreserveRecipients() {
        return preserveRecipients;
    }

    public MandrillConfig setPreserveRecipients(boolean preserveRecipients) {
        this.preserveRecipients = preserveRecipients;
        return this;
    }

    public String getBccAddress() {
        return bccAddress;
    }

    public MandrillConfig setBccAddress(String bccAddress) {
        this.bccAddress = bccAddress;
        return this;
    }

    public String getTrackingDomain() {
        return trackingDomain;
    }

    public MandrillConfig setTrackingDomain(String trackingDomain) {
        this.trackingDomain = trackingDomain;
        return this;
    }

    public boolean isTrackOpens() {
        return trackOpens;
    }

    public MandrillConfig setTrackOpens(boolean trackOpens) {
        this.trackOpens = trackOpens;
        return this;
    }

    public boolean isTrackClicks() {
        return trackClicks;
    }

    public MandrillConfig setTrackClicks(boolean trackClicks) {
        this.trackClicks = trackClicks;
        return this;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public MandrillConfig addTag(String tag) {
        tags.add(tag);
        return this;
    }

    public MandrillConfig addTags(String... tags) {
        return addTags(Arrays.asList(tags));
    }

    public MandrillConfig addTags(Iterable<String> tags) {
        for (String tag : tags) {
            this.tags.add(tag);
        }
        return this;
    }

    public MandrillConfig setTags(String... tags) {
        return setTags(Arrays.asList(tags));
    }

    public MandrillConfig setTags(Iterable<String> tags) {
        this.tags.clear();
        for (String tag : tags) {
            this.tags.add(tag);
        }
        return this;
    }

}
