package org.eventsourcing.sql_storage.action;

import java.util.function.Consumer;

public interface IAction<P> extends Consumer<P> {
}
