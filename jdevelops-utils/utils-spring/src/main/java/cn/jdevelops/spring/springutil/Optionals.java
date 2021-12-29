/*
 * Copyright 2016-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.jdevelops.spring.springutil;

import org.springframework.util.Assert;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Utility methods to work with {@link Optional}s.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 */
public interface Optionals {

	/**
	 * Invokes the given {@link Supplier}s for {@link Optional} results one by one and returns the first non-empty one.
	 *
	 * @param suppliers must not be {@literal null}.
	 */
	@SafeVarargs
	static <T> Optional<T> firstNonEmpty(Supplier<Optional<T>>... suppliers) {

		Assert.notNull(suppliers, "Suppliers must not be null!");

		return firstNonEmpty(Streamable.of(suppliers));
	}

    /**
     * Invokes the given {@link Supplier}s for {@link Optional} results one by one and returns the first non-empty one.
     *
     * @param suppliers must not be {@literal null}.
     */
    static <T> Optional<T> firstNonEmpty(Iterable<Supplier<Optional<T>>> suppliers) {

        Assert.notNull(suppliers, "Suppliers must not be null!");
        return Streamable.of(suppliers).stream()
                .map(Supplier::get)
                .filter(Optional::isPresent)
                .findFirst().orElse(Optional.empty());
    }



}
