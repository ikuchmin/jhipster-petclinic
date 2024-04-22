import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vet.reducer';

export const VetDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vetEntity = useAppSelector(state => state.vet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vetDetailsHeading">
          <Translate contentKey="petClinicApp.vet.detail.title">Vet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vetEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="petClinicApp.vet.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{vetEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="petClinicApp.vet.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{vetEntity.lastName}</dd>
          <dt>
            <span id="salary">
              <Translate contentKey="petClinicApp.vet.salary">Salary</Translate>
            </span>
          </dt>
          <dd>{vetEntity.salary}</dd>
          <dt>
            <Translate contentKey="petClinicApp.vet.specialities">Specialities</Translate>
          </dt>
          <dd>
            {vetEntity.specialities
              ? vetEntity.specialities.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {vetEntity.specialities && i === vetEntity.specialities.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/vet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vet/${vetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VetDetail;
