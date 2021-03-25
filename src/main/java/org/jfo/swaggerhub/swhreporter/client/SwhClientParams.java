package org.jfo.swaggerhub.swhreporter.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class SwhClientParams {
  
  private SwhClientParams(){
    //Private constructor to avoid sonar notification
  }
  
  public static final String OWNER_PARAM = "owner";
  public static final String LIMIT_PARAM = "limit" ;
  public static final String ORDER_PARAM = "order";
  public static final String SORT_PARAM = "sort";
  public static final String PAGE_PARAM = "page";

  private static final String PAGE_SIZE = "25";

  public static MultiValueMap<String, String> buildAllOwnerSpecsParams(String owner, int page) {
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("specType", "ANY");
    queryParams.add("state", "ALL");
    queryParams.add("visibility", "ANY");
    queryParams.add(LIMIT_PARAM, PAGE_SIZE);
    queryParams.add(ORDER_PARAM, "ASC");
    queryParams.add(OWNER_PARAM, owner);
    queryParams.add(PAGE_PARAM, String.valueOf(page));
    queryParams.add(SORT_PARAM, "NAME");
    return queryParams;
  }

  public static MultiValueMap<String, String> buildGetSpecVersionByUrlQueryParams(boolean resolved) {
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("resolved", "" + resolved);
    return queryParams;
  }

  public static MultiValueMap<String, String> buildGetCollaborationQueryParams() {
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("expandTeams", "true");
    return queryParams;
  }
  
  public static Map<String, String> buildGetCollaborationUriParams(String owner, String url) {
    Map<String, String> uriParams = new HashMap<>();
    uriParams.put(OWNER_PARAM, owner);
    uriParams.put("api", getApiNameFromUrl(url, owner));
    return uriParams;
  }

  public static String getApiNameFromUrl(String url, String owner) {
    String base = SwhWebClient.BASE_URL + "/apis/" + owner + "/";
    String removed = StringUtils.remove(url, base);
    return removed.substring(0, removed.indexOf("/"));
  }

  public static Map<String, String> buildGetProjectsUriParams(String owner) {
    Map<String, String> uriParams = new HashMap<>();
    uriParams.put(OWNER_PARAM, owner);
    return uriParams;
  }

  public static MultiValueMap<String, String> buildGetProjectsQueryParams(int page) {
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("nameOnly", "false");
    queryParams.add(PAGE_PARAM, String.valueOf(page));
    queryParams.add(LIMIT_PARAM, PAGE_SIZE);
    queryParams.add(ORDER_PARAM, "ASC");
    return queryParams;
  }

  public static Map<String, String> buildGetProjectMembersUriParams(String owner, String project) {
    Map<String, String> uriParams = new HashMap<>();
    uriParams.put(OWNER_PARAM, owner);
    uriParams.put("projectId", project);
    return uriParams;
  }
}
