package com.rafi.esb.infra.db.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@SuppressWarnings("JpaDataSourceORMInspection")
@Data
@Accessors(chain = true)
@Entity
@Table(name = "routes")
public class RouteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "contextId", nullable = false)
    private String contextId;

    @Column(name = "sourceName", nullable = false)
    private String sourceName;

    @Column(name = "sourceId", nullable = false)
    private String sourceId;

    @Column(name = "origRequest", nullable = false)
    private String origRequest;

    @Column(name = "transformedRequest")
    private String transformedRequest;

    @Column(name = "targetName", nullable = false)
    private String targetName;

    @Column(name = "matchingId", nullable = false)
    private String matchingId;

    @Column(name = "origResponse")
    private String origResponse;

    @Column(name = "transformedResponse")
    private String transformedResponse;

    @Column(name = "origSecResponse")
    private String origSecResponse;

    @Column(name = "transformedSecResponse")
    private String transformedSecResponse;

    @Column(name = "secMatchingId")
    private String secMatchingId;
}
