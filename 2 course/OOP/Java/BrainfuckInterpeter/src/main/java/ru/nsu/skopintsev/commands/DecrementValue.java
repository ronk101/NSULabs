package ru.nsu.skopintsev.commands;

import ru.nsu.skopintsev.Data;

public class DecrementValue implements Commands{
    @Override
    public void execute(Data data) {
        data.setByte((byte) (data.getByte() - 1));
    }
}
