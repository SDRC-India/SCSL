package org.sdrc.scsl.util;

/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 24-Apr-2017 6:59:07 pm
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * @author Subhadarshani 
 */
public class Constants {

	public static final class Mobile {
		public static final String INVALID_CREDENTIALS = "invalid.credentials";
		public static final String INVALID_USER_AREA_MAPPING = "invalid.user.area.mapping";
		public static final String INVALID_USER_ROLE_MAPPING = "invalid.user.role.mapping";
		public static final String DEO_ROLE_CODE = "deo.role.code";
		public static final String AHI_ASSOCIATE_ROLE_CODE = "ahi.associate.role.code";
		public static final String INVALID_USER_TYPE_LOGIN = "invalid.user.type.login";
		public static final String INVALID_DATA = "invalid.data";
		public static final String SUBMISSION_DEADLINE_DATE = "submission.deadline.date";
		public static final String REJECTION_SUBMISSION_DEADLINE_DATE_BY_SUP = "rejection.submission.deadline.date.by.sup";
		public static final String REJECTION_SUBMISSION_DEADLINE_DATE_BY_MNE = "rejection.submission.deadline.date.by.mne";
		public static final String INVALID_MONTH_DATA = "invalid.month.data";
		public static final String DATA_PRESENT_FOR_TIME_PERIOD = "data.present.for.time_period";
		public static final String STATUS_REJECTED = "status.rejected";
		public static final String IFTM_PRESENT = "iftm.present";
		
		public static final String SYSCONFIG_RETURN_ZERO_OBJECT_ERROR_CODE="syscofig.return.zero.object.error";
		public static final String SYSCONFIG_RETURN_ZERO_OBJECT_ERROR_MESSAGE ="syscofig.return.zero.object.error.message";
		public static final String API_VERSION_MISMATCH_ERROR_CODE="api.version.mismatch.error.code";
		public static final String API_VERSION_MISMATCH_ERROR_MESAGE="api.version.mismatch.error.message" ;
		public static final String API_VERSION_MISSING_IN_SYSCONFIG_ERROR_CODE="api.version.missing.sysconfig.error.code";
		public static final String API_VERSION_MISSING_IN_SYSCONFIG_MESAGE="api.version.missing.sysconfig.error.message" ;
		
	}

	public static final class Web {
		public static final String USER_PRINCIPAL = "UserPrincipal";
		public static final String INVALID_CREDENTIALS = "invalid.credentials";
		public static final String SNCU_SUBMISSION_FAILED = "sncu.submission.failed";
		public static final String SNCU_SUBMISSION_SUCCESS = "sncu.submission.success";
		public static final String SUCCESS_STATUS_CODE = "success.status.code";
		public static final String FAILURE_STATUS_CODE = "failure.status.code";
		public static final String SUBMISSION_ERROR_MESSAGE = "submission.error.message";
		public static final String SUBMISSION_INVALID_ERROR_MESSAGE = "submission.invalid.error.message";
		public static final String FILE_PATH_PDSA = "filepath.pdsa";
		public static final String STATUS_CODE_PDSA_NOT_CLOSED = "not.closed.status.id";
		public static final String FREQUENCY_MONTHLY = "frequency.monthly";
		public static final String FREQUENCY_DAILY = "frequency.daily";
		public static final String FREQUENCY_WEEKLY = "frequency.weekly";
		public static final String FREQUENCY_BI_WEEKLY = "frequency.bi.weekly";
		public static final String STATUS_CODE_PDSA_CLOSED = "close.status.id";
		public static final String TYPE_ID_STATUS = "type.status";
		public static final String ONGOING_STATUS = "ongoing.status";
		public static final String PENDING_STATUS = "pending.status";
		public static final String ADAPT_STATUS = "adapt.status";
		public static final String ADOPT_STATUS = "adopt.status";
		public static final String ABANDON_STATUS = "abandon.status";
		public static final String ONGOING_STATUS_CSS = "ongoing.status.css";
		public static final String PENDING_STATUS_CSS = "pending.status.css";
		public static final String ADAPT_STATUS_CSS = "adapt.status.css";
		public static final String ADOPT_STATUS_CSS = "adopt.status.css";
		public static final String ABANDON_STATUS_CSS = "abandon.status.css";

		public static final String INVALID_USERNAME_PASSWORD = "invalid.username.password";
		public static final String SUCCESS_LOGGED_OUT = "success.logged.out";

		public static final String SUBMISSION_STATUS_PENDING = "submission.pending.status";
		public static final String SUBMISSION_STATUS_AUTO_APPROVED = "submission.auto.approved.status";
		public static final String SUBMISSION_AUTO_APPROVED_CREATED_BY = "submission.created.auto";
		public static final String TIMEPERIOD_PREODICITY_MONTHLY = "timeperiod.monthly.periodicity";
		public static final String TIMEPERIOD_QUARTER_PERIODICITY = "timeperiod.quarter.periodicity";
		public static final String TIMEPERIOD_YEAR_PERIODICITY = "timeperiod.year.periodicity";
		public static final String SUBMISSION_STATUS_APPROVED = "submission.approved.status";
		public static final String SUBMISSION_STATUS_REJECTED = "submission.rejected.status";
		public static final String SUBMISSION_LEGACY_STATUS = "submission.legacy.status";

		public static final String SUBMISSION_APPROVED_BY_MnE = "status.approved.MnE";
		public static final String SUBMISSION_REJECTED_BY_MnE = "status.rejected.MnE";
		public static final String SUBMISSION_APPROVED_BY_SUPRITENDENT = "status.approved.supritendent";
		public static final String SUBMISSION_REJECTED_BY_SUPRITENDENT = "status.rejected.supritendent";
		public static final String SUBMISSION_PENDING = "status.pending";
		public static final String SUBMISSION_OLD_MESSAGE = "submission.old.message";
		public static final String PENDING_STATUS_TYPE_NAME = "pending.status.type.name";

		public static final String ACCESS_DENIED = "accessDenied";
		public static final String UNAUTHORIZED = "unauthorized";
		
		public static final String LAST_DATE_SUP = "date.lastdate.sup";
		public static final String AFTER_DATE_15 = "date.after";
		public static final String SUPERINTENDENT_APPROVAL_LASTDATE = "superintendent.approval.lastdate";
		public static final String MNE_APPROVAL_LASTDATE = "mne.approval.lastdate";

		// aggregation
		public static final String FACILITY_LEVEL = "facility.level";
		public static final String DISTRICT_LEVEL = "district.level";
		public static final String STATE_LEVEL = "state.level";
		public static final String COUNTRY_LEVEL = "country.level";

		// india id
		public static final String INDIA_AREA_ID = "india.area.id";

		public static final String EXT = "file.extension";
		public static final String HISTORICAL_FILE_PATH_EXCEL = "historical.file.path.excel";
		public static final String HISTORICAL_FILE_EXTENSION = "historical.file.extension";
		public static final String HISTORICAL_LR_FILE_READ_PATH = "historical.lr.file.read.path";
		public static final String HISTORICAL_NOLR_FILE_READ_PATH = "historical.nolr.file.read.path";
		public static final String RAW_DATA_XLS_TEMPLATE = "raw.data.xls.template";
		public static final String AGGREGATE_DATA_XLS_TEMPLATE = "aggregate.data.xls.template";
		public static final String INDICATOR_TYPE_PROCESS = "indicator.type.process";
		public static final String INDICATOR_TYPE_PROCESS_NAME = "indicator.type.process.name";
		public static final String FREQUENCY_TYPE_ID = "frequency.type.id";

		public static final String PDSA_ADD_SUCCESS = "pdsa.save.successfull";
		public static final String PDSA_ADD_FAILED = "pdsa.save.failure";

		public static final String BOOTSTRAP_ALERT_SUCCESS = "bootstrap.alert.success";
		public static final String BOOTSTRAP_ALERT_DANGER = "bootstrap.alert.danger";
		public static final String TXNPDSA_ADD_SUCCESS = "txn.pdsa.success";
		public static final String TXNPDSA_ADD_FAILED = "txn.pdsa.failure";

		public static final String PDSA_CLOSE_SUCCESS = "pdsa.close.success";
		public static final String PDSA_CLOSE_ERROR = "pdsa.close.failed";
		public static final String PDSA_FOLDER = "filepath.pdsa.folder";

		public static final String RECORD_TYPE = "record.type";

		public static final String FACILITY_TYPEID = "facility.type.id";

		// submission date crossed message

		public static final String LAST_DATE_SUBMISSION_CROSSED_MESSAGE = "last.date.submission.crossed.message";

		public static final String LOGIN_META_ID = "loginMetaId";

		// role name
		public static final String SUPERINTENDENT_ROLE_NAME = "superintendent.role.name";
		public static final String MNE_ROLE_NAME = "mne.role.name";

		// indicator type
		public static final String INDICATOR_TYPE_INTERMEDIATE_NAME = "indicator.type.intermediate";
		public static final String INDICATOR_TYPE_INTERMEDIATE = "indicator.type.intermediate.id";
		public static final String INDICATOR_TYPE_OUTCOME = "indicator.type.outcome.id";

		public static final String FACILITY_LEVEL_ID = "facility.level.id";
		public static final String DISTRICT_LEVEL_ID = "district.level.id";
		public static final String STATE_LEVEL_ID = "state.level.id";
		public static final String COUNTRY_LEVEL_ID = "country.level.id";

		// mail detail
		public static final String SMTP_HOST_KEY = "smtp.host.key";
		public static final String SOCKETFACTORY_PORT_KEY = "socketFactory.port.key";
		public static final String SOCKETFACTORY_CLASS_KEY = "socketFactory.class.key";
		public static final String SMTP_AUTH_KEY = "smtp.auth.key";
		public static final String SMTP_PORT_KEY = "smtp.port.key";

		public static final String SMTP_HOST = "smtp.host";
		public static final String SOCKETFACTORY_PORT = "socketFactory.port";
		public static final String SOCKETFACTORY_CLASS = "socketFactory.class";
		public static final String SMTP_AUTH = "smtp.auth";
		public static final String SMTP_PORT = "smtp.port";
		public static final String AUTHENTICATION_USERID = "authentication.userid";
		public static final String AUTHENTICATION_PASSWORD = "authentication.password";
		public static final String MESSAGE_SETFORM = "message.setFrom";

		public static final String SUBMISSION_APPROVED_CSS = "submissionApproved.css";
		public static final String SUBMISSION_REJECTED_CSS = "submissionRejected.css";
		public static final String SUBMISSION_PENDING_CSS = "submissionPending.css";
		
		public static final String AHI_ASSOCIATE_ROLE_ID="ahi.associate.role.id";
		public static final String AHI_PROJECT_MANAGER="ahi.project.manager";
		public static final String AHI_MNE_LEAD="ahi.mne.lead.id";
		public static final String AHI_OPERATION_LEAD_ROLE_ID="ahi.operation.lead.role.id";
		
		public static final String STATUS_AUTO_APPROVED_SUPERITENDENT="status.auto.approved.superitendent";
		public static final String STATUS_LEGACY_MNE="status.legacy.mne";
		
		public static final String SUBMISSION_APPROVED_MESSAGE = "submission.approved.message";
		public static final String SUBMISSION_REJECTED_MESSAGE = "submission.rejected.message";
		
		public static final String PLANNING_RELEASE_SUCCESS="planning.release.success";
		
		public static final String PLANNING_RELEASE_FAILURE="planning.release.failure";
		
		public static final String FILEPATH_PLANNING_FOLDER="filepath.planning.folder";
		public static final String SNCU_SUBMISSION_OLD_MESSAGE = "sncu.submission.old.message";
		public static final String FILEPATH_PLANNING_TRIP_FOLDER = "filepath.planning.trip.folder";
		public static final String FILEPATH_PLANNING_AGENDA_FOLDER = "filepath.planning.agenda.folder";
		public static final String FAILURE_STATUS_PLANNING = "planning.failure.message";
		public static final String SUCCESS_STATUS_PLANNING = "planning.success.message";
		public static final String FAILURE_STATUS_PLANNING_MAILLING = "mail.planning.failure.message";
		public static final String FAILURE_STATUS_RELEASE_MAILLING = "mail.release.failure.message";
		
		public static final String FAILURE_STATUS_PLANNING_CLOSE = "planning.failure.close";
		public static final String SUCCESS_STATUS_PLANNING_CLOSE = "planning.success.close";
		
		public static final String PLANNING_REPORT_TEMPLATE = "planning.report.xls.template";
		
		//facility type, size, total typedetail ids
		public static final String TOTAL_TYPEDETAIL_ID = "total.typedetail.id";
		public static final String PUBLIC_TYPEDETAIL_ID = "public.typedetail.id";
		public static final String PRIVATE_TYPEDETAIL_ID = "private.typedetail.id";
		public static final String SMALL_PRIVATE_TYPEDETAIL_ID = "small.private.typedetail.id";
		public static final String LARGE_PRIVATE_TYPEDETAIL_ID = "large.private.typedetail.id";
		public static final String SMALL_PUBLIC_TYPEDETAIL_ID = "small.public.typedetail.id";
		public static final String LARGE_PUBLIC_TYPEDETAIL_ID = "large.public.typedetail.id";
		
//		#txnSNCUDATA description message type detail id
		public static final String WARNING_DESCRIPTION_MESSAGE_FIRST = "warning.description.message.first";
		public static final String WARNING_DESCRIPTION_MESSAGE_SECOND = "warning.description.message.second";
		public static final String WARNING_DESCRIPTION_MESSAGE_THIRD = "warning.description.message.third";

//		2016-jan, 2016-dec 
		public static final String JANUARY_2016_TIMEPERIOD_ID = "january.2016.timeperiod.id";
		public static final String DECEMBER_2016_TIMEPERIOD_ID = "december.2016.timeperiod.id";
		
		public static final String HISTORICAL_TEMPLATE_VERSION = "historical.template.version";
		public static final String HISTORICAL_TEMPLATE_INVALID_ERROR_MESSAGE = "historical.template.invalid.error.message";
		public static final String HISTORICAL_TEMPLATE_SALT = "salt";

		
		public static final String EMAIL_DISCLAIMER = "email.disclaimer";
		public static final String PASSWORD_CHANGE_SUBJECT = "password.change.subject";
		
		//files uploaded from historical data module. both submitted and rejected ones will be stored
		public static final String HISTORICAL_FILE_UPLOADED_PATH = "historical.file.uploaded.path";
		public static final String HISTORICAL_FILE_UPLOADED = "uploaded";
		public static final String HISTORICAL_FILE_REJECTED = "rejected";
		public static final String HISTORICAL_TEMPLATE_DISABLE_CONTENT_ERROR_MESSAGE = "historical.template.disable.content.error.message";
		public static final String HISTORICAL_FILE_EXCEPTION_TYPE = "Error";
		public static final String ENGAGEMENT_SCORE_STARTING_TIMEPERIOD_ID = "engagement.score.starting.timeperiod.id";
		
		public static final String SCSL_PROGRAM_NAME = "scsl.program.name";
		public static final String DATE_MONTH_YEAR_FORMATTER = "date.month.year.formatter";
		public static final String DATE_MONTH_YEAR_SIMPLEDATE_FORMATTER = "date.month.year.simpledate.formatter";
		public static final String DATE_MONTH_YEAR_FORMATTER_NO_HYPHEN = "date.month.year.formatter.no.hyphen";
		public static final String MONTH_YEAR_FORMATTER_NO_HYPHEN = "month.year.formatter.no.hyphen";
		public static final String MONTH_SMALL_YEAR_FORMATTER_NO_HYPHEN = "month.small.year.formatter.no.hyphen";
		public static final String YEAR_MONTH_DATE_FORMATTER_HYPHEN = "year.month.date.formatter.hyphen";
		public static final String YEAR_MONTH_DATE_TIME_FORMATTER_NO_HYPHEN = "year.month.date.time.formatter.no.hyphen";
		public static final String YEAR_MONTH_DATE_TIME_FORMATTER_HYPHEN = "year.month.date.time.formatter.hyphen";
		
		//#scsl chart folder name added
		public static final String SCSL_CHART_FOLDER_NAME = "scsl.chart.folder.name";
	}
}
