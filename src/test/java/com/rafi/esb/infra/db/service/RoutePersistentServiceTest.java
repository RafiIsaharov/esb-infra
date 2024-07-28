package com.rafi.esb.infra.db.service;

import com.rafi.esb.infra.db.model.RouteEntity;
import com.rafi.esb.infra.tools.HumanReadableTestNames;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
@Transactional
@DisplayNameGeneration(HumanReadableTestNames.class)
class RoutePersistentServiceTest {

    @Inject
    RoutePersistentService routePersistentService;

    @BeforeEach
    @AfterEach
    void cleanUp() {
        routePersistentService.deleteAll();
    }

    /** Test insert **/
    @Test
    void successInsertForMinFields() {
        RouteEntity routeEnt = new RouteEntity();

        routeEnt.setContextId("GET_RATE")
                .setOrigRequest("Original request")
                .setSourceId("1234")
                .setSourceName("P2G")
                .setMatchingId("4567")
                .setTargetName("WellsFargo");

        String retUuid = routePersistentService.insert(routeEnt);
        assertThat(recordWithUuidExists(retUuid)).isTrue();
    }

    @Test
    void successInsertForMaxFields() {
        RouteEntity routeEnt = aRoute();

        String retUuid = routePersistentService.insert(routeEnt);
        assertThat(recordWithUuidExists(retUuid)).isTrue();
    }

    @Test
    void insertFails_WhenNotAllMandatoryFieldsAreSupplied() {
        //TargetName is mandatory but was not supplied
        RouteEntity routeEnt = aRoute();

        routeEnt.setTargetName(null);

        //assertThrows(PropertyValueException.class,
        //        ()->routePersistentService.insert(routeEnt));

        assertThatThrownBy(()->routePersistentService.insert(routeEnt))
                .isInstanceOf(PropertyValueException.class);

    }

    /** Test select **/
    @Test
    void successSelect() {
        RouteEntity routeEnt = aRoute();

        String retUuid = routePersistentService.insert(routeEnt);
        RouteEntity selectedRouteEnt = routePersistentService.select(retUuid);

        assertThat(selectedRouteEnt).isNotNull();
        assertThat(routeEnt).isEqualTo(selectedRouteEnt);
    }

    @Test
    void failsSelectRoute_WhenItDoesNotExist() {
        String uuid = "randomuuid";
        RouteEntity selectedRouteEnt = routePersistentService.select(uuid);

        assertThat(selectedRouteEnt).isNull();
    }

    /** Test update **/
    @Test
    void failsUpdateRoute_WhenItExists() {
        RouteEntity routeEnt = aRoute();

        String retUuid = routePersistentService.insert(routeEnt);

        String updateValue = "Transformed response was updated";
        routeEnt.setTransformedResponse(updateValue);
        routePersistentService.update(routeEnt);

        RouteEntity selectedRouteEnt = routePersistentService.select(retUuid);

        assertThat(selectedRouteEnt).isNotNull();
        assertThat(updateValue).isEqualTo(selectedRouteEnt.getTransformedResponse());
    }

    @Test
    void failsUpdateRoute_WhenRouteDoesNotExists() {
        RouteEntity routeEnt = aRoute();

        boolean updateResult = routePersistentService.update(routeEnt);

        assertThat(updateResult).isFalse();
    }

    @Test
    void successUpdateByUUID() {
        RouteEntity routeEnt = aRoute();

        String retUuid = routePersistentService.insert(routeEnt);

        String updateValue = "transformed request updated";
        routePersistentService.updateTransformedRequest(retUuid, updateValue);
        RouteEntity selectedRouteEnt = routePersistentService.select(retUuid);
        assertThat(selectedRouteEnt).isNotNull();
        assertThat(updateValue).isEqualTo(selectedRouteEnt.getTransformedRequest());

        updateValue = "orig response updated";
        routePersistentService.updateOrigResponse(retUuid, updateValue);
        selectedRouteEnt = routePersistentService.select(retUuid);
        assertThat(selectedRouteEnt).isNotNull();
        assertThat(updateValue).isEqualTo(selectedRouteEnt.getOrigResponse());

        updateValue = "transformed response updated";
        routePersistentService.updateTransformedResponse(retUuid, updateValue);
        selectedRouteEnt = routePersistentService.select(retUuid);
        assertThat(selectedRouteEnt).isNotNull();
        assertThat(updateValue).isEqualTo(selectedRouteEnt.getTransformedResponse());

        updateValue = "second orig response updated";
        routePersistentService.updateOrigSecResponse(retUuid, updateValue);
        selectedRouteEnt = routePersistentService.select(retUuid);
        assertThat(selectedRouteEnt).isNotNull();
        assertThat(updateValue).isEqualTo(selectedRouteEnt.getOrigSecResponse());

        updateValue = "transformed second response updated";
        routePersistentService.updateTransformedSecResponse(retUuid, updateValue);
        selectedRouteEnt = routePersistentService.select(retUuid);
        assertThat(selectedRouteEnt).isNotNull();
        assertThat(updateValue).isEqualTo(selectedRouteEnt.getTransformedSecResponse());

    }

    /** Test delete **/
    @Test
    @DisplayName("Delete passes when deleting by existing id")
    void successDelete() {
        RouteEntity routeEnt = aRoute();

        String retUuid = routePersistentService.insert(routeEnt);
        assertThat(recordWithUuidExists(retUuid)).isTrue();

        routePersistentService.deleteRouteEntity(routeEnt);
        assertThat(recordWithUuidExists(retUuid)).isFalse();

    }

    @Test
    void successDelete_WhenDeletingByExistingId() {
        RouteEntity routeEnt = aRoute();

        String retUuid = routePersistentService.insert(routeEnt);
        assertTrue(recordWithUuidExists(retUuid));

        routePersistentService.deleteRouteById(retUuid);
        assertThat(recordWithUuidExists(retUuid)).isFalse();
    }

    /** helpers **/
    boolean recordWithUuidExists(String uuid) {
        return routePersistentService.select(uuid) != null;

    }

    RouteEntity aRoute(){
        return new RouteEntity()
                .setContextId("GET_RATE")
                .setOrigRequest("Original request")
                .setTransformedRequest("Transformed request")
                .setSourceId("1234")
                .setSourceName("P2G")
                .setMatchingId("4567")
                .setTargetName("WellsFargo")
                .setOrigResponse("Original Response")
                .setTransformedResponse("Transformed response")
                .setOrigSecResponse("Orig second Response")
                .setTransformedSecResponse("Transformed second response");
    }

}