/*
 * Copyright (C) 2025 Rostislav Suleimanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

<?php
class ArrayAutovivificationTest
{
    public function nonCompliantExamples(): void
    {
        $data = false;
        $data['key'] = 'value'; // Noncompliant {{Deprecated: Automatic conversion of false to array is deprecated.}}

        $collection = false;
        $collection[] = 'item'; // Noncompliant {{Deprecated: Automatic conversion of false to array is deprecated.}}

        $nested = false;
        $nested['first']['second'] = 'deeply nested'; // Noncompliant {{Deprecated: Automatic conversion of false to array is deprecated.}}

        // Non-compliant case in a loop
        $results = false;
        foreach (range(1, 5) as $number) {
            $results[$number] = $number * 2; // Noncompliant {{Deprecated: Automatic conversion of false to array is deprecated.}}
        }

        $maybeData = false;
        if (rand(0, 1)) {
            $maybeData['conditionally'] = 'assigned'; // Noncompliant {{Deprecated: Automatic conversion of false to array is deprecated.}}
        }
    }

    public function compliantExamples(): void
    {

        $data = [];
        $data['key'] = 'value'; // Compliant - Initialize as empty array


        $collection = [];
        $collection[] = 'item'; // Compliant - Array push on properly initialized array


        $nested = [];
        $nested['first']['second'] = 'deeply nested'; // Compliant - Initialize as empty array then nested access


        $results = [];
        foreach (range(1, 5) as $number) {
            $results[$number] = $number * 2; // Compliant - Proper initialization in a loop context
        }


        $condition = rand(0, 1);
        $maybeData = $condition ? [] : null;
        if ($condition) {
            $maybeData['conditionally'] = 'assigned'; // Compliant - Conditional creation then access
        }


        $dataOrFalse = $this->getMaybeArray();
        if ($dataOrFalse !== false) {
            $dataOrFalse['key'] = 'value'; // Compliant - Explicit check before array access
        }
    }

    public function mixedExamples(): void
    {
        $items = false;
        $items['first'] = 'value1'; // Noncompliant {{Deprecated: Automatic conversion of false to array is deprecated.}}


        $products = [];
        $products['name'] = 'Example Product'; // Compliant case - proper initialization

        $config = false;
        if ($this->shouldHaveConfig()) {
            $config['setting'] = $this->getDefaultSetting(); // Noncompliant {{Deprecated: Automatic conversion of false to array is deprecated.}}
        }

        $userSettings = $this->hasUserSettings() ? [] : false;
        if ($this->hasUserSettings()) {
            $userSettings['preference'] = 'default'; // Compliant
        }

        $result = $this->processItems();
        if ($result !== false) {
            foreach ($result as $item) {
                echo $item;
            }
        }
    }

    private function getMaybeArray()
    {
        return rand(0, 1) ? ['example' => 'data'] : false;
    }

    private function shouldHaveConfig(): bool
    {
        return true;
    }

    private function getDefaultSetting(): string
    {
        return 'default';
    }

    private function hasUserSettings(): bool
    {
        return rand(0, 1) > 0;
    }

    private function processItems()
    {
        return rand(0, 1) ? ['item1', 'item2'] : false;
    }
}