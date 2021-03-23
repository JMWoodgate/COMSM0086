package com.company.DBCommand;

import com.company.DBExceptions.DBException;
import com.company.DBExceptions.StorageType;
import com.company.DBExceptions.StorageTypeException;

public interface DBCommand {
    public void execute() throws StorageTypeException, DBException;
    public int getIndex();
    public StorageType getType();
}
