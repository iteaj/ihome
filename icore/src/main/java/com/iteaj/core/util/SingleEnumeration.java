package com.iteaj.core.util;

import java.util.Enumeration;

public class SingleEnumeration<E> implements Enumeration<E> {

    private E obj;
    private int count = 1;

    public SingleEnumeration(E obj) {
        this.obj = obj;
    }

    @Override
    public boolean hasMoreElements() {
        if(count==0)
            return false;
        return true;
    }

    @Override
    public E nextElement() {
        count --;
        return obj;
    }
}
