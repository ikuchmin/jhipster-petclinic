import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Vet from './vet';
import Speciality from './speciality';
import Pet from './pet';
import PetType from './pet-type';
import Owner from './owner';
import Visit from './visit';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="vet/*" element={<Vet />} />
        <Route path="speciality/*" element={<Speciality />} />
        <Route path="pet/*" element={<Pet />} />
        <Route path="pet-type/*" element={<PetType />} />
        <Route path="owner/*" element={<Owner />} />
        <Route path="visit/*" element={<Visit />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
