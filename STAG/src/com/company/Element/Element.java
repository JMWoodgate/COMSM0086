package com.company.Element;

import com.company.Visitor.Visitor;

public interface Element {

    void accept(Visitor visitor);
    String getName();
    String getDescription();
}
