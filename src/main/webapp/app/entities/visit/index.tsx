import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Visit from './visit';
import VisitDetail from './visit-detail';
import VisitUpdate from './visit-update';
import VisitDeleteDialog from './visit-delete-dialog';

const VisitRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Visit />} />
    <Route path="new" element={<VisitUpdate />} />
    <Route path=":id">
      <Route index element={<VisitDetail />} />
      <Route path="edit" element={<VisitUpdate />} />
      <Route path="delete" element={<VisitDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VisitRoutes;
