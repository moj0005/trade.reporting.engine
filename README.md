# Getting Started

## Setup Instructions

- Java version 1.8.0_281 is used
- It's an IntelliJ IDEA project
- Once checkout this project, Application.java file should run
- Kindly refer to trade.reporting.engine.png to have a look at the DB structure
- Upload the xml file, post request to http://localhost:8080/api/v1/events, form-data should be selected, key should be file, and value should be the file being chosen from the local directory to process.
- Get request to http://localhost:8080/api/v1/events?fileName=<name of the file being uploaded>, this will tell whether it satisfies pre-configured criteria or not.
- trade.reporting.engine.png file is available in the base directory to have a look at the database design.
- Update database username and password in application.properties file. Make sure to have TradeReportingEngine defined datbase in mysql, as defined in application.properties file.
- No need to create tables, tables will automatically be created once the application is started. Also the tables will be dropped once the application is shutdown.
- To view logs, refer to app.log available in the base directory.

## DB Design

- Filter - defines the name of the filter and the expression to be used for applying this filter on documents
- FilterEvent - Once the file is uploaded, it is against an event. Hence all filters are applied to that uploaded file/event and being stored in FilterEvent with the extraced value
- FilterCriteria - pre-defined/pre-configured criteria against a filter. This criteria has a value which is to be evaluated against filter events.
- FilterCriteriaExecutionGroup - Multiple criterias (conditions) can be defined. This FilterCriteriaExecutionGroup groups them in a single group, e.g., GROUP1 may contain 2 FilterCriterias (currently AND condition) and GROUP2 may contain another 2 FilterCriterias. Against any event, if any of the criteria is satisfied, it's relevant values are returned as described in the document. 