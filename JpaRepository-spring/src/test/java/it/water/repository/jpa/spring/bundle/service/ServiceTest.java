
/*
 * Copyright 2024 Aristide Cittadino
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.water.repository.jpa.spring.bundle.service;

import it.water.core.api.registry.ComponentRegistry;
import it.water.core.api.service.Service;
import it.water.core.interceptors.annotations.Inject;
import org.springframework.stereotype.Component;


@Component
public class ServiceTest implements Service {

    //Using this annotation to check if interceptors works correctly
    //normally inside Spring context you can user Autowired
    @Inject
    private ComponentRegistry registry;

    public void setRegistry(ComponentRegistry registry) {
        this.registry = registry;
    }

    public ComponentRegistry getRegistry() {
        return registry;
    }
}
