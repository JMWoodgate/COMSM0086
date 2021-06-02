package com.company.Element;

import com.company.Visitor.Visitor;

public class Artefact implements Element{
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
