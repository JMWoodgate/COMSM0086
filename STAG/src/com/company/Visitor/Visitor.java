package com.company.Visitor;

import com.company.Element.Element;
import com.company.Element.Location;

public interface Visitor {

    void visit(Element element);
    void visit(Location location);
}
