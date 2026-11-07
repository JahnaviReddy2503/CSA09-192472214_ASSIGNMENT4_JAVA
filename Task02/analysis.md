# Map versus database

Loading `m` doctors into a map costs `O(m)` once and `O(m + d)` memory for
doctors and `d` department keys. Each change costs `O(r)` to repopulate the
combo for `r` doctors, without network latency. A database query per change
costs query/network latency plus `O(r)` transfer and temporary memory, but
reflects current data. The map is better for a stable directory; a refresh
policy is needed when data changes.
