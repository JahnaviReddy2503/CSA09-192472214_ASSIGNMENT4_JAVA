# Comparison

| Style | Readability | Field access | Reuse | Large-app recommendation |
|---|---|---|---|---|
| Frame implements | Simple tiny class | Direct | Central method | Avoid for large frames |
| Named inner | Explicit name | Outer fields | Strong | Good medium choice |
| Anonymous | Local but verbose | `Outer.this` | Store object to reuse | Small one-off action |
| Lambda | Concise | Captures fields | Excellent when stored | Good for short actions |

All use `O(1)` conversion work and `O(1)` listener memory per trigger. A shared
Action minimizes objects when many triggers share one command.
