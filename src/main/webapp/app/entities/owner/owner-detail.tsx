import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './owner.reducer';

export const OwnerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ownerEntity = useAppSelector(state => state.owner.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ownerDetailsHeading">
          <Translate contentKey="petClinicApp.owner.detail.title">Owner</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ownerEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="petClinicApp.owner.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{ownerEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="petClinicApp.owner.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{ownerEntity.lastName}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="petClinicApp.owner.email">Email</Translate>
            </span>
          </dt>
          <dd>{ownerEntity.email}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="petClinicApp.owner.address">Address</Translate>
            </span>
          </dt>
          <dd>{ownerEntity.address}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="petClinicApp.owner.city">City</Translate>
            </span>
          </dt>
          <dd>{ownerEntity.city}</dd>
          <dt>
            <span id="telephone">
              <Translate contentKey="petClinicApp.owner.telephone">Telephone</Translate>
            </span>
          </dt>
          <dd>{ownerEntity.telephone}</dd>
        </dl>
        <Button tag={Link} to="/owner" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/owner/${ownerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OwnerDetail;
