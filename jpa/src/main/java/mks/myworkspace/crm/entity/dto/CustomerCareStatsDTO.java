package mks.myworkspace.crm.entity.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CustomerCareStatsDTO {
	private Long totalCustomerCares;
    private Long notCaredCount; //Chua cham soc
    private Long caredOnTimeCount; //Cham soc dung han
    private Long caredLateCount; //Cham soc tre han
    private Map<String, Long> statusDistribution;
    private Map<String, Long> priorityDistribution;
    private Map<String, Long> careTimeliness;
    private List<TimeSeriesData> careTrend; //xu huong cham soc theo thoi gian
    //private List<EmployeePerformance> topPerformers;
    private Double averageResponseTime;
    private Map<String, Double> radarData; // key: responsiblePersonName, value: avg care delay (in hours)
}

@Data
class TimeSeriesData{
	private Long date;
	private Long count;
}

//@Data
//class EmployeePerformance {
//    private String employeeName;
//    private Long customerCount;
//}
