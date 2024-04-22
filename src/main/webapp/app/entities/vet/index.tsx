import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Vet from './vet';
import VetDetail from './vet-detail';
import VetUpdate from './vet-update';
import VetDeleteDialog from './vet-delete-dialog';

const VetRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Vet />} />
    <Route path="new" element={<VetUpdate />} />
    <Route path=":id">
      <Route index element={<VetDetail />} />
      <Route path="edit" element={<VetUpdate />} />
      <Route path="delete" element={<VetDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VetRoutes;
