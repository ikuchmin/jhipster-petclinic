import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './speciality.reducer';

export const SpecialityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const specialityEntity = useAppSelector(state => state.speciality.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="specialityDetailsHeading">
          <Translate contentKey="petClinicApp.speciality.detail.title">Speciality</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{specialityEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="petClinicApp.speciality.name">Name</Translate>
            </span>
          </dt>
          <dd>{specialityEntity.name}</dd>
          <dt>
            <Translate contentKey="petClinicApp.speciality.vet">Vet</Translate>
          </dt>
          <dd>
            {specialityEntity.vets
              ? specialityEntity.vets.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {specialityEntity.vets && i === specialityEntity.vets.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/speciality" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/speciality/${specialityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SpecialityDetail;
