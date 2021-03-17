package com.company.Parsing;

import com.company.DBExceptions.StorageType;

public interface DBCommand {
    public void execute();
    public int getIndex();
    public StorageType getType();
}
