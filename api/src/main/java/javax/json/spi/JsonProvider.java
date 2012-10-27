/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.json.spi;

import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.ServiceLoader;

import javax.json.JsonArray;
import javax.json.JsonConfiguration;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

/**
 * Service provider for JSON objects.
 *
 * @see ServiceLoader
 * @author Jitendra Kotamraju
 */
public abstract class JsonProvider {

    /**
     * A constant representing the name of the default
     * {@code JsonProvider} implementation class.
     */
    private static final String DEFAULT_PROVIDER
            = "com.alibaba.json.JsonProviderImpl";

    protected JsonProvider() {
    }

    /**
     *
     * Creates a JSON provider object. The provider is loaded using
     * {@link ServiceLoader#load(Class)} method. If there is no available
     * service provider is found, then the default service provider
     * is loaded using the current thread's
     * {@linkplain java.lang.Thread#getContextClassLoader context class loader}.
     *
     * @see ServiceLoader
     * @return a JSON provider
     */
    public static JsonProvider provider() {
        ServiceLoader<JsonProvider> loader = ServiceLoader.load(JsonProvider.class);
        Iterator<JsonProvider> it = loader.iterator();
        if (it.hasNext()) {
            return it.next();
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Class<?> clazz = (classLoader == null)
                ? Class.forName(DEFAULT_PROVIDER)
                : classLoader.loadClass(DEFAULT_PROVIDER);
            return (JsonProvider)clazz.newInstance();
        } catch (ClassNotFoundException x) {
            throw new JsonException(
                    "Provider " + DEFAULT_PROVIDER + " not found", x);
        } catch (Exception x) {
            throw new JsonException(
                    "Provider " + DEFAULT_PROVIDER + " could not be instantiated: " + x,
                    x);
        }
    }

    /**
     * Creates a JSON parser from the specified character stream
     *
     * @param reader i/o reader from which JSON is to be read
     */
    public abstract JsonParser createParser(Reader reader);

    /**
     * Creates a JSON parser from the specified character stream. The created
     * parser is configured with the specified configuration.
     *
     * @param reader i/o reader from which JSON is to be read
     * @param config configuration of the parser
     */
    public abstract JsonParser createParser(Reader reader, JsonConfiguration config);


    /**
     * Creates a JSON parser from the specified JSON array.
     *
     * @param array JSON array
     */
    public abstract JsonParser createParser(JsonArray array);

    /**
     * Creates a JSON parser from the specified JSON array. The created
     * parser is configured with the specified configuration.
     *
     * @param array JSON array
     * @param config configuration of the parser
     */
    public abstract JsonParser createParser(JsonArray array, JsonConfiguration config);

    /**
     * Creates a JSON parser from the specified JSON object.
     *
     * @param object JSON object
     */
    public abstract JsonParser createParser(JsonObject object);

    /**
     * Creates a JSON parser from the specified JSON object. The created
     * parser is configured with the specified configuration.
     *
     * @param object JSON object
     * @param config configuration of the parser
     */
    public abstract JsonParser createParser(JsonObject object, JsonConfiguration config);

    /**
     * Creates a JSON generator which can be used to write JSON text to the
     * specified character stream.
     *
     * @param writer a i/o writer to which JSON is written
     */
    public abstract JsonGenerator createGenerator(Writer writer);

    /**
     * Creates a JSON generator which can be used to write JSON text to the
     * specified character stream. The created generator is configured
     * with the specified configuration.
     *
     * @param writer i/o writer to which JSON is written
     * @param config configuration of the generator
     */
    public abstract JsonGenerator createGenerator(Writer writer, JsonConfiguration config);


}