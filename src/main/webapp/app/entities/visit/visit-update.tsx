import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVet } from 'app/shared/model/vet.model';
import { getEntities as getVets } from 'app/entities/vet/vet.reducer';
import { IPet } from 'app/shared/model/pet.model';
import { getEntities as getPets } from 'app/entities/pet/pet.reducer';
import { IVisit } from 'app/shared/model/visit.model';
import { getEntity, updateEntity, createEntity, reset } from './visit.reducer';

export const VisitUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vets = useAppSelector(state => state.vet.entities);
  const pets = useAppSelector(state => state.pet.entities);
  const visitEntity = useAppSelector(state => state.visit.entity);
  const loading = useAppSelector(state => state.visit.loading);
  const updating = useAppSelector(state => state.visit.updating);
  const updateSuccess = useAppSelector(state => state.visit.updateSuccess);

  const handleClose = () => {
    navigate('/visit' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getVets({}));
    dispatch(getPets({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...visitEntity,
      ...values,
      vet: vets.find(it => it.id.toString() === values.vet?.toString()),
      pet: pets.find(it => it.id.toString() === values.pet?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...visitEntity,
          vet: visitEntity?.vet?.id,
          pet: visitEntity?.pet?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="petClinicApp.visit.home.createOrEditLabel" data-cy="VisitCreateUpdateHeading">
            <Translate contentKey="petClinicApp.visit.home.createOrEditLabel">Create or edit a Visit</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="visit-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('petClinicApp.visit.date')} id="visit-date" name="date" data-cy="date" type="date" />
              <ValidatedField id="visit-vet" name="vet" data-cy="vet" label={translate('petClinicApp.visit.vet')} type="select">
                <option value="" key="0" />
                {vets
                  ? vets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="visit-pet" name="pet" data-cy="pet" label={translate('petClinicApp.visit.pet')} type="select">
                <option value="" key="0" />
                {pets
                  ? pets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/visit" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default VisitUpdate;
