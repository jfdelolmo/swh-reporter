package org.jfo.swaggerhub.swhreporter.mappers;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.exception.SwaggerParseResultException;
import org.jfo.swaggerhub.swhreporter.model.db.Api;
import org.jfo.swaggerhub.swhreporter.model.db.Domain;
import org.jfo.swaggerhub.swhreporter.model.db.NewCollaboration;
import org.jfo.swaggerhub.swhreporter.model.db.NewMember;
import org.jfo.swaggerhub.swhreporter.model.db.NewProperties;
import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
import org.jfo.swaggerhub.swhreporter.model.db.NewTeam;
import org.jfo.swaggerhub.swhreporter.model.db.ProjectParticipant;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonProperty;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.jfo.swaggerhub.swhreporter.model.swh.CollaborationMembership;
import org.jfo.swaggerhub.swhreporter.model.swh.CollaborationTeamMembership;
import org.jfo.swaggerhub.swhreporter.model.swh.Project;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMember;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

@Component
public class SwhMapper {

  private static final String API_TYPE = "API";
  private static final String DOMAIN_TYPE = "DOMAIN";
  private static final int MAX_DESCRIPTION_LENGTH = 15;
  private static final int SPEC_NAME_POSITION = 3;

  public NewSpecification apisJsonApiToSpecModel(ApisJsonApi apisJsonApi) {
    NewSpecification specification = new NewSpecification();

    NewProperties p = mapToProperties(apisJsonApi.getProperties());
    specification.setProperties(p);

    specification.setName(extractNameFromUrl(p.getUrl()));
    specification.setTitle(apisJsonApi.getName());
    int minSize = Math.min(MAX_DESCRIPTION_LENGTH, apisJsonApi.getDescription().length());
    specification.setDescription(StringUtils.isEmpty(apisJsonApi.getDescription()) ?
        "No description provided" :
        apisJsonApi.getDescription().substring(0, minSize) + "..."
    );

    return specification;
  }

  public String extractNameFromUrl(String url) {
    String[] segments = StringUtils.remove(url, SwhWebClient.BASE_URL).split("/");
    return segments[SPEC_NAME_POSITION];
  }

  public NewProperties mapToProperties(List<ApisJsonProperty> input) {
    NewProperties properties = new NewProperties();
    input.forEach(p -> {
      if ("Swagger".equalsIgnoreCase(p.getType())) {
        properties.setType("API");
        properties.setUrl(p.getUrl());
      }
      if ("X-Domain".equalsIgnoreCase(p.getType())) {
        properties.setType("DOMAIN");
        properties.setUrl(p.getUrl());
      }
      if ("X-Version".equalsIgnoreCase(p.getType())) {
        properties.setVersion(p.getValue());
      }
      if ("X-Created".equalsIgnoreCase(p.getType())) {
        properties.setCreated(OffsetDateTime.parse(p.getValue()));
      }
      if ("X-Created".equalsIgnoreCase(p.getType())) {
        properties.setModified(OffsetDateTime.parse(p.getValue()));
      }
    });
    return properties;
  }

  public NewCollaboration collaborationSwhToModel(Collaboration input) {
    NewCollaboration output = new NewCollaboration();

    input.getMembers().forEach(m -> output.addMember(memberSwhToModel(m)));
    input.getTeams().forEach(t -> output.addTeam(teamSwhToModel(t)));

    return output;
  }

  private NewMember memberSwhToModel(CollaborationMembership collaborationMembership) {
    NewMember member = new NewMember();
    member.setName(collaborationMembership.getName());
    member.setRole(getMainRole(collaborationMembership.getRoles()));
    return member;
  }

  private String getMainRole(List<CollaborationMembership.RolesEnum> roles) {

    if (roles.contains(CollaborationMembership.RolesEnum.EDIT)) {
      return "Designer";
    } else if (roles.contains(CollaborationMembership.RolesEnum.COMMENT)) {
      return "Collaborator";
    } else {
      return "Viewer";
    }
  }

  private NewTeam teamSwhToModel(CollaborationTeamMembership collaborationTeam) {
    NewTeam team = new NewTeam();
    team.setName(collaborationTeam.getName());
    team.setDescription(collaborationTeam.getTitle());
    return team;
  }

  public OpenAPI parseOpenApi(String apiAsString) throws SwaggerParseResultException {
    OpenAPIV3Parser openAPIV3Parser = new OpenAPIV3Parser();
    ParseOptions options = new ParseOptions();
    options.setResolveFully(true);
    SwaggerParseResult parseResults = openAPIV3Parser.readContents(apiAsString, null, options);
    if (!parseResults.getMessages().isEmpty()) {
      throw new SwaggerParseResultException("Exception on parsing api", parseResults.getMessages());
    }
    return parseResults.getOpenAPI();
  }

//  public NewOpenApiDocument specificationAsStringToOpenApiDocument(String specificationAsString) throws Exception {
//    NewOpenApiDocument openApiDocument = new NewOpenApiDocument();
//
////    openApiDocument.setDefinition(ClobProxy.generateProxy(specificationAsString));
//    openApiDocument.setDefinition(specificationAsString);
//    return openApiDocument;
//  }

  private byte[] openApiToByteArray(OpenAPI openAPI) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(openAPI);
    oos.flush();
    return baos.toByteArray();
  }

  private String apiStringYaml(String specificationAsString) {
    return new Yaml().dump(specificationAsString);
  }


  public org.jfo.swaggerhub.swhreporter.model.db.Project projectSwhToModel(Project project) {
    org.jfo.swaggerhub.swhreporter.model.db.Project dbProject = new org.jfo.swaggerhub.swhreporter.model.db.Project();
    dbProject.setName(project.getName());
    dbProject.setDescription(project.getDescription());
    project.getApis().forEach(api -> dbProject.addApi(new Api(api)));
    project.getDomains().forEach(api -> dbProject.addDomain(new Domain(api)));
    return dbProject;
  }
  
  public ProjectParticipant memberShwToParticipants(ProjectMember member){
    ProjectParticipant participant = new ProjectParticipant();
    participant.setName(member.getName());
    participant.setType(member.getType().getValue());
    participant.setRoles(member
        .getRoles()
        .stream()
        .map(ProjectMember.RolesEnum::getValue)
        .collect(Collectors.joining(", "))
    );
    
    return participant;
    
  }
}
