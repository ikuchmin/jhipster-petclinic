import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PetType from './pet-type';
import PetTypeDetail from './pet-type-detail';
import PetTypeUpdate from './pet-type-update';
import PetTypeDeleteDialog from './pet-type-delete-dialog';

const PetTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PetType />} />
    <Route path="new" element={<PetTypeUpdate />} />
    <Route path=":id">
      <Route index element={<PetTypeDetail />} />
      <Route path="edit" element={<PetTypeUpdate />} />
      <Route path="delete" element={<PetTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PetTypeRoutes;
