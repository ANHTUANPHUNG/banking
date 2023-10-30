package com.cg.service;

import java.util.List;

public interface IGeneralService<E, T> {

    List<E> findAll();

    E findById(T t);

    void create(E e);

    void update(T t, E e);

    boolean removeById(T t);
}
