# data-river

Replicates data from mysql to any datastore of your choice (relies on maxwell)

Imagine you have 2 tables in MySql, `Table1` and `Table2`. You would have to
configure [maxwell](http://maxwells-daemon.io). Once maxwell is properly configured, lets say you
want to persist changes in the above tables in [Elastic-Search](https://www.elastic.co). We would
create 2 rivers, `table1_river` and `table2_river`. Then for each of these `riverType`, provide
implementation of `IFilter`, `IEnricher` and `IBackFiller` and build a
`TransformerFactory`

Incoming kafka message goes through following 3 steps:

1.
Filtering: `IFilter`
governs whether the incoming message should be processed or not.
2.
Enrichment: `IEnricher`
provides a way to enrich the incoming message. (think of joining the row with some other row)
3.
Persistence: `IPersister`
persists the message in your desired data store.

There is also and admin task on the admin port of your dropwizard application which can be used to
backfill the data. Example

```
http://localhost:8080/admin/backfill?startDate=2016-01-01T00:00:00&endDate=2016-01-10T00:00:00&riverType=river1
```
