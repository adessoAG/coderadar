'use strict';

/**
 * This is a stateful service that stores some global state for the web page and provides accessors to this state.
 * @constructor
 * @param _$filter_ Angular $filter service.
 */
Coderadar.LabelProvider = function (_$filter_) {
    this.$filter = _$filter_;
};

/**
 * Creates a human readable label for a commit.
 * @param {Commit} commit The Commit for which to create a label.
 * @returns {string} The label for the specified commit.
 */
Coderadar.LabelProvider.prototype.getLabelForCommit = function (commit) {
    var date = new Date(commit.timestamp);
    return this.$filter('date')(date, 'MMMM dd, yyyy \'at\' HH:mm:ss') + " - " + commit.id;
};

/**
 * Creates a human readable label for a metric.
 * @param metric The Metric for which to create a label.
 * @returns {string} The label for the specified metric.
 */
Coderadar.LabelProvider.prototype.getLabelForMetric = function (metric){
  return metric.displayName;
};

// registering service with angular
angular.module('coderadarApp')
    .service('LabelProvider', ['$filter', Coderadar.LabelProvider]);