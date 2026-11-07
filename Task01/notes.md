# Task 01 - GUI Design Review

## 1.1 Design deficiencies in the given `FeeWindow` code

| Deficiency | User experience or failure |
|---|---|
| `==` compares the action command string | The Pay or Receipt branch may not run because `==` compares object identity rather than text content. The click can appear to do nothing. |
| All actions are handled by one listener | One large conditional listener becomes difficult to read, test, and extend. A change to one button can accidentally affect another button. |
| No input validation or `NumberFormatException` handling | Empty, non-numeric, zero, or negative amounts can crash the action or produce invalid payments instead of a helpful message. |
| SQL is built by string concatenation | A malicious value can inject SQL, corrupt the query, or expose/delete data. Ordinary names containing apostrophes can also break the query. |
| Database work and `Thread.sleep(3000)` run on the EDT | The window freezes for at least three seconds; it cannot repaint, accept input, or respond to closing while the work runs. |
| No user feedback is shown | Users cannot tell whether payment is processing, succeeded, or failed, so they may click repeatedly or assume the application is broken. |
| The Receipt button is not handled | A user who completes payment cannot view proof of payment, so an advertised feature is unusable. |
| The database connection is never closed | Repeated use leaks connections and may eventually exhaust database resources or prevent clean application shutdown. |
| No window-closing handling exists | The application may remain running after the window is closed, or resources may not be released in a controlled way. |
| Checked exceptions are not handled at the UI boundary | The code may fail to compile, or runtime/database errors may escape without an understandable message to the user. |

## 1.2 Redesign

### Class-structure diagram

```text
Main
  |
  +--> FeeWindow (Swing UI, fields, labels, buttons, listeners)
          |
          +--> SwingWorker
                  |
                  +--> FeeService (validation and payment use case)
                          |
                          +--> PaymentRepository
                                  +--> InMemoryPaymentRepository (default)
                                  +--> JdbcPaymentRepository (optional)

FeeService --> PaymentResult (success, message, receipt text)
```

`FeeWindow` only collects input and displays status/receipt results. `FeeService`
validates the input and delegates persistence. The default `Main` uses
`InMemoryPaymentRepository`, so the program runs without MySQL. The optional
`JdbcPaymentRepository` uses `PreparedStatement`, and its caller owns the
connection lifecycle.

The Pay, Clear, and Receipt buttons each have their own listener. Clear resets
the input fields and any previous receipt. Payment runs in a
`SwingWorker`, while `done()` updates Swing components on the EDT. A status
label communicates progress and outcomes. A `WindowAdapter` disposes the
window cleanly when the user closes it.

## 1.3 Design defence (96 words)

The redesign separates presentation, business rules, and persistence, so each
part can be tested or replaced independently. Validation in `FeeService`
prevents malformed payments before they reach a repository. The repository
interface makes the default program deterministic and MySQL-independent while
still providing a safe `PreparedStatement` adapter. `SwingWorker` keeps the
interface responsive and returns completion handling to Swing’s EDT. Dedicated
button listeners remove command-string dispatch errors and improve
maintainability. Status text and receipts make outcomes visible to users.
Window-closing handling provides predictable lifecycle behavior and resource
cleanup.

## Listener design comparison

For `k` buttons, one command listener may perform up to `k` sequential
comparisons, so dispatch is `O(k)` in the worst case and it centralizes
branching. Separate listeners dispatch directly to the selected button's
handler in `O(1)` expected time, at the cost of `O(k)` listener objects and
references. The space difference is linear either way for `k` controls; the
separate-listener design gives clearer responsibility and avoids fragile string
dispatch.

## Practical execution record

**Aim:** Collect fee details, validate them, process a payment without freezing
the UI, and display a receipt.

**Requirements:** Java 17+, Swing, `SwingWorker`, `PreparedStatement` for the
optional JDBC adapter, and try-with-resources.

**Algorithm:** Read fields; validate name, register number, and positive
amount; run the repository operation in a worker; update status; display the
receipt when requested.

| Test | Input/action | Expected result | Verification |
|---|---|---|---|
| Valid | Asha Rao / CSE01 / 1250, Pay Fee | Success status and receipt | Service smoke test passed |
| Invalid amount | `bad` | Valid-number message | Service smoke test passed |
| Negative amount | `-1` | Greater-than-zero message | Service smoke test passed |
| Clear | Click Clear after entering values | Fields and prior receipt reset | GUI manual test |
