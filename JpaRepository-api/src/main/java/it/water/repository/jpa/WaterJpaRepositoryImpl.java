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

package it.water.repository.jpa;

import it.water.core.api.model.BaseEntity;
import it.water.core.api.model.PaginableResult;
import it.water.core.api.repository.query.Query;
import it.water.core.api.repository.query.QueryBuilder;
import it.water.core.api.repository.query.QueryOrder;
import it.water.core.interceptors.annotations.Inject;
import it.water.repository.jpa.api.JpaRepository;
import it.water.repository.jpa.api.JpaRepositoryManager;
import it.water.repository.jpa.api.WaterJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.Setter;

import java.util.function.Consumer;
import java.util.function.Function;


/**
 * @Author Aristide Cittadino
 * This class represents a framework repository object which must be materialized in specific technologies.
 * Example: if you create a framework (water) bundle with an entity. This module could be instantiated in spring,osgi,quarkus.
 * For every technology it must be used in they way the specific technology supports.
 * <p>
 * This wrapper allows to "attach" a concrete repository instance at runtime.
 * It must be always an BaseJpaRepositoryImpl object.
 */
public class WaterJpaRepositoryImpl<T extends BaseEntity> implements WaterJpaRepository<T> {
    private JpaRepository<T> concreteRepository;
    private Class<T> type;
    private String persistenceUnitName;
    @Inject
    @Setter
    private JpaRepositoryManager jpaRepositoryManager;

    public WaterJpaRepositoryImpl(Class<T> type, String persistenceUnitName) {
        this.type = type;
        this.persistenceUnitName = persistenceUnitName;
    }

    private JpaRepository<T> getConcreteRepository() {
        if (this.concreteRepository == null) {
            this.concreteRepository = jpaRepositoryManager.createConcreteRepository(type, persistenceUnitName);
        }
        return this.concreteRepository;
    }

    protected Class<T> getType() {
        return type;
    }

    protected String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    @Override
    public Class<T> getEntityType() {
        return this.type;
    }

    @Override
    public T persist(T entity) {
        return this.persist(entity, null);
    }

    @Override
    public T persist(T entity, Runnable runnable) {
        return getConcreteRepository().persist(entity, runnable);
    }

    @Override
    public T update(T entity) {
        return this.update(entity, null);
    }

    @Override
    public T update(T entity, Runnable runnable) {
        return getConcreteRepository().update(entity, runnable);
    }

    @Override
    public void remove(long id) {
        remove(id, null);
    }

    @Override
    public void remove(long id, Runnable runnable) {
        getConcreteRepository().remove(id, runnable);
    }

    @Override
    public void remove(T entity) {
        getConcreteRepository().remove(entity);
    }

    @Override
    public void removeAllByIds(Iterable<Long> ids) {
        getConcreteRepository().removeAllByIds(ids);
    }

    @Override
    public void removeAll(Iterable<T> entities) {
        getConcreteRepository().removeAll(entities);
    }

    @Override
    public void removeAll() {
        getConcreteRepository().removeAll();
    }

    @Override
    public T find(long id) {
        return getConcreteRepository().find(id);
    }

    @Override
    public T find(String filterStr) {
        return getConcreteRepository().find(filterStr);
    }

    @Override
    public T find(Query filter) {
        return getConcreteRepository().find(filter);
    }

    @Override
    public PaginableResult<T> findAll(int delta, int page, Query filter, QueryOrder queryOrder) {
        return getConcreteRepository().findAll(delta, page, filter, queryOrder);
    }

    @Override
    public long countAll(Query filter) {
        return getConcreteRepository().countAll(filter);
    }

    @Override
    public QueryBuilder getQueryBuilderInstance() {
        return getConcreteRepository().getQueryBuilderInstance();
    }

    @Override
    public EntityManager getEntityManager() {
        return getConcreteRepository().getEntityManager();
    }

    @Override
    public void txExpr(Transactional.TxType txType, Consumer<EntityManager> function) {
        getConcreteRepository().txExpr(txType, function);
    }

    @Override
    public <R> R tx(Transactional.TxType txType, Function<EntityManager, R> function) {
        return getConcreteRepository().tx(txType, function);
    }
}
