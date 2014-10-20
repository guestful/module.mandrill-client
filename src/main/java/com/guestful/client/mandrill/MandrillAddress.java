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
import javax.json.JsonObjectBuilder;
import java.util.regex.Pattern;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MandrillAddress {

    // See for email validation: http://www.regular-expressions.info/email.html
    private static final Pattern PATTERN_EMAIL = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);

    private final String name;
    private final String email;

    public MandrillAddress(String email) {
        this(email, email);
    }

    public MandrillAddress(String name, String email) {
        if (email == null) throw new NullPointerException();
        if (!isValidEmail(email)) throw new IllegalArgumentException("Invalid email: '" + email + "'");
        this.name = name;
        this.email = email;
    }

    public JsonObjectBuilder toJson() {
        JsonObjectBuilder _to = Json.createObjectBuilder().add("email", getEmail());
        if (getName() != null) _to.add("name", getName());
        return _to;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return getName() + " <" + getEmail() + ">";
    }

    public static boolean isValidEmail(String email) {
        return email != null && PATTERN_EMAIL.matcher(email).matches();
    }
}
