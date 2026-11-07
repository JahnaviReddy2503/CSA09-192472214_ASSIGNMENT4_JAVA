# Stages 2-4 design

```text
RegistrationFrame (ui) -> RegistrationService (service) -> RegistrationDao (dao)
                                                        -> DbConnection
RegistrationFrame -> JTable / SwingWorker
```

```text
Register -> validate -> service duplicate check -> DAO transaction
Filter -> reset page -> SwingWorker -> LIMIT/OFFSET -> table model
```

The UI never executes SQL on the EDT. The DAO uses PreparedStatement and
try-with-resources. The database UNIQUE constraint remains authoritative for
concurrent duplicates. Loading 100,000 rows at once costs O(n) memory and
causes a large initial model update; pages cost O(p) model memory and keep
individual DB calls off the EDT, though deep OFFSET scans can grow slower.
