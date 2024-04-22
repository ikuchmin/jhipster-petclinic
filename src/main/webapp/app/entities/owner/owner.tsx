import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './owner.reducer';

export const Owner = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const ownerList = useAppSelector(state => state.owner.entities);
  const loading = useAppSelector(state => state.owner.loading);
  const links = useAppSelector(state => state.owner.links);
  const updateSuccess = useAppSelector(state => state.owner.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="owner-heading" data-cy="OwnerHeading">
        <Translate contentKey="petClinicApp.owner.home.title">Owners</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="petClinicApp.owner.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/owner/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="petClinicApp.owner.home.createLabel">Create new Owner</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={ownerList ? ownerList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {ownerList && ownerList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="petClinicApp.owner.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('firstName')}>
                    <Translate contentKey="petClinicApp.owner.firstName">First Name</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('firstName')} />
                  </th>
                  <th className="hand" onClick={sort('lastName')}>
                    <Translate contentKey="petClinicApp.owner.lastName">Last Name</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('lastName')} />
                  </th>
                  <th className="hand" onClick={sort('email')}>
                    <Translate contentKey="petClinicApp.owner.email">Email</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                  </th>
                  <th className="hand" onClick={sort('address')}>
                    <Translate contentKey="petClinicApp.owner.address">Address</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('address')} />
                  </th>
                  <th className="hand" onClick={sort('city')}>
                    <Translate contentKey="petClinicApp.owner.city">City</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('city')} />
                  </th>
                  <th className="hand" onClick={sort('telephone')}>
                    <Translate contentKey="petClinicApp.owner.telephone">Telephone</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('telephone')} />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {ownerList.map((owner, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/owner/${owner.id}`} color="link" size="sm">
                        {owner.id}
                      </Button>
                    </td>
                    <td>{owner.firstName}</td>
                    <td>{owner.lastName}</td>
                    <td>{owner.email}</td>
                    <td>{owner.address}</td>
                    <td>{owner.city}</td>
                    <td>{owner.telephone}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/owner/${owner.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/owner/${owner.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/owner/${owner.id}/delete`)}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="petClinicApp.owner.home.notFound">No Owners found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Owner;
