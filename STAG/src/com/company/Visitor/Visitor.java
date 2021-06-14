package com.company.Visitor;

import com.company.Element.*;
import com.company.Element.Character;

public interface Visitor {

    void visit(Element element);
    void visit(Location location);
    void visit(Artefact artefact);
    void visit(Character characters);
    void visit(Furniture furniture);
    void visit(Player players);
}
