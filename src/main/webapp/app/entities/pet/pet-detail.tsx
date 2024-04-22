import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pet.reducer';

export const PetDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const petEntity = useAppSelector(state => state.pet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="petDetailsHeading">
          <Translate contentKey="petClinicApp.pet.detail.title">Pet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{petEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="petClinicApp.pet.name">Name</Translate>
            </span>
          </dt>
          <dd>{petEntity.name}</dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="petClinicApp.pet.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>{petEntity.birthDate ? <TextFormat value={petEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="petClinicApp.pet.type">Type</Translate>
          </dt>
          <dd>{petEntity.type ? petEntity.type.id : ''}</dd>
          <dt>
            <Translate contentKey="petClinicApp.pet.owner">Owner</Translate>
          </dt>
          <dd>{petEntity.owner ? petEntity.owner.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/pet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pet/${petEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PetDetail;
