/*
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.stanford.laneweb;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.selection.Selector;

import java.util.Map;

/**
 * stolen from org.apache.cocoon.selection.ParameterSelector.java 
 * @author ceyates
 *
 */
public class RegexParameterSelector extends AbstractLogEnabled implements ThreadSafe, Selector {

    public boolean select(String regex, Map objectModel, Parameters parameters) {
        String compareToString = parameters.getParameter("parameter-selector-test", null);
        getLogger().debug(regex);
        getLogger().debug(compareToString);
        getLogger().debug(Boolean.toString(compareToString.matches(regex)));
        return compareToString != null && compareToString.matches(regex);
    }
}
