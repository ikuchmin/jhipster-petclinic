import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPetType } from 'app/shared/model/pet-type.model';
import { getEntities as getPetTypes } from 'app/entities/pet-type/pet-type.reducer';
import { IOwner } from 'app/shared/model/owner.model';
import { getEntities as getOwners } from 'app/entities/owner/owner.reducer';
import { IPet } from 'app/shared/model/pet.model';
import { getEntity, updateEntity, createEntity, reset } from './pet.reducer';

export const PetUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const petTypes = useAppSelector(state => state.petType.entities);
  const owners = useAppSelector(state => state.owner.entities);
  const petEntity = useAppSelector(state => state.pet.entity);
  const loading = useAppSelector(state => state.pet.loading);
  const updating = useAppSelector(state => state.pet.updating);
  const updateSuccess = useAppSelector(state => state.pet.updateSuccess);

  const handleClose = () => {
    navigate('/pet' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPetTypes({}));
    dispatch(getOwners({}));
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
      ...petEntity,
      ...values,
      type: petTypes.find(it => it.id.toString() === values.type?.toString()),
      owner: owners.find(it => it.id.toString() === values.owner?.toString()),
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
          ...petEntity,
          type: petEntity?.type?.id,
          owner: petEntity?.owner?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="petClinicApp.pet.home.createOrEditLabel" data-cy="PetCreateUpdateHeading">
            <Translate contentKey="petClinicApp.pet.home.createOrEditLabel">Create or edit a Pet</Translate>
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
                  id="pet-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('petClinicApp.pet.name')}
                id="pet-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('petClinicApp.pet.birthDate')}
                id="pet-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="date"
              />
              <ValidatedField id="pet-type" name="type" data-cy="type" label={translate('petClinicApp.pet.type')} type="select">
                <option value="" key="0" />
                {petTypes
                  ? petTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="pet-owner" name="owner" data-cy="owner" label={translate('petClinicApp.pet.owner')} type="select">
                <option value="" key="0" />
                {owners
                  ? owners.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pet" replace color="info">
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

export default PetUpdate;
